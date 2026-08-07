#!/usr/bin/env python3
"""Ask a local Ollama-compatible model for AdaptiveFlowGrid parameters.

This is a development-only tool. It uses only Python's standard library and is
not imported by, packaged with, or required by the KitFlow runtime.
"""

from __future__ import annotations

import argparse
import ipaddress
import json
import os
import sys
import urllib.error
import urllib.parse
import urllib.request
from typing import Any, Mapping, Optional, Sequence


DEFAULT_ENDPOINT = "http://127.0.0.1:11434/api/chat"
DEFAULT_MODEL = "qwen2.5:0.5b"
MAX_RESPONSE_BYTES = 256 * 1024

ADVICE_KEYS = frozenset(
    {
        "minColumnWidthDp",
        "maxColumns",
        "horizontalSpacingDp",
        "verticalSpacingDp",
        "fontScaleAware",
    }
)

# Ollama accepts a JSON Schema object in the `format` field. Keeping this schema
# small makes it practical for small local models and gives us a deterministic
# contract to validate again before generating Kotlin.
ADVICE_SCHEMA: dict[str, Any] = {
    "type": "object",
    "properties": {
        "minColumnWidthDp": {
            "type": "integer",
            "minimum": 48,
            "maximum": 1200,
        },
        "maxColumns": {"type": "integer", "minimum": 1, "maximum": 12},
        "horizontalSpacingDp": {
            "type": "integer",
            "minimum": 0,
            "maximum": 64,
        },
        "verticalSpacingDp": {
            "type": "integer",
            "minimum": 0,
            "maximum": 64,
        },
        "fontScaleAware": {"type": "boolean"},
    },
    "required": sorted(ADVICE_KEYS),
    "additionalProperties": False,
}

SYSTEM_PROMPT = """You advise developers configuring KitFlow's AdaptiveFlowGrid.
Return only JSON matching the supplied schema. Choose conservative values that:
- keep cards readable at every supplied viewport width,
- avoid excessive columns on wide screens,
- preserve usable spacing on narrow screens, and
- account for the supplied font scales.
All dimensions are density-independent pixels (dp). Do not emit Kotlin, prose,
Markdown, extra keys, decimal numbers, or units."""


class AdvisorError(Exception):
    """An expected, user-facing layout-advisor failure."""


class _NoRedirectHandler(urllib.request.HTTPRedirectHandler):
    """Keep a loopback request from being redirected to another host."""

    def redirect_request(
        self,
        req: urllib.request.Request,
        fp: Any,
        code: int,
        msg: str,
        headers: Mapping[str, str],
        newurl: str,
    ) -> None:
        return None


def _strict_int(value: Any) -> bool:
    """Return true for an int but not bool (bool subclasses int in Python)."""

    return isinstance(value, int) and not isinstance(value, bool)


def validate_advice(value: Any) -> dict[str, Any]:
    """Validate untrusted model output against ADVICE_SCHEMA.

    Validation is deliberately implemented locally instead of adding a JSON
    Schema package to this zero-dependency developer tool.
    """

    if not isinstance(value, dict):
        raise AdvisorError("model response must be a JSON object")

    keys = set(value)
    missing = ADVICE_KEYS - keys
    extra = keys - ADVICE_KEYS
    if missing:
        raise AdvisorError(
            "model response is missing required field(s): " + ", ".join(sorted(missing))
        )
    if extra:
        raise AdvisorError(
            "model response contains unexpected field(s): " + ", ".join(sorted(extra))
        )

    integer_ranges = {
        "minColumnWidthDp": (48, 1200),
        "maxColumns": (1, 12),
        "horizontalSpacingDp": (0, 64),
        "verticalSpacingDp": (0, 64),
    }
    for name, (minimum, maximum) in integer_ranges.items():
        field_value = value[name]
        if not _strict_int(field_value):
            raise AdvisorError(f"model response field {name!r} must be an integer")
        if not minimum <= field_value <= maximum:
            raise AdvisorError(
                f"model response field {name!r} must be between {minimum} and {maximum}"
            )

    if not isinstance(value["fontScaleAware"], bool):
        raise AdvisorError("model response field 'fontScaleAware' must be a boolean")

    # Return a new dictionary in stable field order rather than retaining any
    # unusual dict subclass supplied by a caller.
    return {
        "minColumnWidthDp": value["minColumnWidthDp"],
        "maxColumns": value["maxColumns"],
        "horizontalSpacingDp": value["horizontalSpacingDp"],
        "verticalSpacingDp": value["verticalSpacingDp"],
        "fontScaleAware": value["fontScaleAware"],
    }


def validate_endpoint(endpoint: str) -> str:
    """Require an explicit loopback HTTP(S) /api/chat URL."""

    try:
        parsed = urllib.parse.urlsplit(endpoint)
        port = parsed.port  # Accessing this also validates the port syntax.
    except ValueError as exc:
        raise AdvisorError(f"invalid endpoint: {exc}") from exc

    if parsed.scheme not in {"http", "https"}:
        raise AdvisorError("endpoint scheme must be http or https")
    if not parsed.hostname:
        raise AdvisorError("endpoint must include a host")
    if parsed.username is not None or parsed.password is not None:
        raise AdvisorError("endpoint must not contain credentials")
    if parsed.query or parsed.fragment:
        raise AdvisorError("endpoint must not contain a query string or fragment")
    if parsed.path.rstrip("/") != "/api/chat":
        raise AdvisorError("endpoint path must be /api/chat")

    hostname = parsed.hostname.lower()
    if hostname.endswith("."):
        raise AdvisorError("endpoint host must not end with a dot")
    try:
        is_loopback = ipaddress.ip_address(hostname).is_loopback
    except ValueError:
        is_loopback = False
    if not is_loopback:
        raise AdvisorError(
            "endpoint must use a numeric loopback address (127.0.0.0/8 or ::1)"
        )
    if port is None or port < 1:
        raise AdvisorError("endpoint must include an explicit non-zero port")

    return endpoint


def parse_integer_list(raw: str, *, option: str, minimum: int, maximum: int) -> list[int]:
    """Parse a non-empty comma-separated list of bounded integers."""

    parts = [part.strip() for part in raw.split(",")]
    if not parts or any(not part for part in parts):
        raise AdvisorError(f"{option} must be a comma-separated list of integers")

    try:
        values = [int(part) for part in parts]
    except ValueError as exc:
        raise AdvisorError(f"{option} must contain only integers") from exc
    if any(value < minimum or value > maximum for value in values):
        raise AdvisorError(
            f"each {option} value must be between {minimum} and {maximum}"
        )
    return values


def parse_float_list(
    raw: str, *, option: str, minimum: float, maximum: float
) -> list[float]:
    """Parse a non-empty comma-separated list of finite bounded floats."""

    parts = [part.strip() for part in raw.split(",")]
    if not parts or any(not part for part in parts):
        raise AdvisorError(f"{option} must be a comma-separated list of numbers")

    try:
        values = [float(part) for part in parts]
    except ValueError as exc:
        raise AdvisorError(f"{option} must contain only numbers") from exc
    if any(
        value != value or value in {float("inf"), float("-inf")}
        for value in values
    ):
        raise AdvisorError(f"{option} values must be finite")
    if any(value < minimum or value > maximum for value in values):
        raise AdvisorError(
            f"each {option} value must be between {minimum:g} and {maximum:g}"
        )
    return values


def build_request_payload(
    *,
    model: str,
    description: str,
    screen_widths_dp: Sequence[int],
    font_scales: Sequence[float],
) -> dict[str, Any]:
    """Build the schema-constrained Ollama /api/chat request."""

    clean_model = model.strip()
    if not clean_model:
        raise AdvisorError("model must not be empty")
    if len(clean_model) > 200:
        raise AdvisorError("model must be at most 200 characters")

    clean_description = description.strip()
    if not clean_description:
        raise AdvisorError("description must not be empty")
    if len(clean_description) > 8_000:
        raise AdvisorError("description must be at most 8,000 characters")

    context = {
        "layoutDescription": clean_description,
        "screenWidthsDp": list(screen_widths_dp),
        "fontScales": list(font_scales),
        "targetApi": "AdaptiveFlowGrid",
    }
    return {
        "model": clean_model,
        "stream": False,
        "format": ADVICE_SCHEMA,
        "options": {"temperature": 0},
        "messages": [
            {"role": "system", "content": SYSTEM_PROMPT},
            {
                "role": "user",
                "content": json.dumps(context, separators=(",", ":"), ensure_ascii=False),
            },
        ],
    }


def request_advice(
    *, endpoint: str, payload: Mapping[str, Any], timeout_seconds: float
) -> dict[str, Any]:
    """Call the local chat endpoint and return strictly validated advice."""

    validate_endpoint(endpoint)
    if not 0 < timeout_seconds <= 600:
        raise AdvisorError("timeout must be greater than 0 and no more than 600 seconds")

    body = json.dumps(payload, separators=(",", ":"), ensure_ascii=False).encode("utf-8")
    request = urllib.request.Request(
        endpoint,
        data=body,
        headers={
            "Content-Type": "application/json",
            "Accept": "application/json",
            "User-Agent": "KitFlow-layout-advisor/1",
        },
        method="POST",
    )

    try:
        # Ignore system proxy settings as well as redirects so a request
        # validated as loopback stays on the local machine.
        opener = urllib.request.build_opener(
            urllib.request.ProxyHandler({}), _NoRedirectHandler()
        )
        with opener.open(request, timeout=timeout_seconds) as response:
            response_bytes = response.read(MAX_RESPONSE_BYTES + 1)
    except urllib.error.HTTPError as exc:
        detail = exc.read(4096).decode("utf-8", errors="replace").strip()
        suffix = f": {detail}" if detail else ""
        raise AdvisorError(f"local model server returned HTTP {exc.code}{suffix}") from exc
    except urllib.error.URLError as exc:
        raise AdvisorError(f"could not reach local model server: {exc.reason}") from exc
    except TimeoutError as exc:
        raise AdvisorError("local model request timed out") from exc

    if len(response_bytes) > MAX_RESPONSE_BYTES:
        raise AdvisorError("local model response exceeded the 256 KiB safety limit")

    try:
        envelope = json.loads(response_bytes.decode("utf-8"))
    except (UnicodeDecodeError, json.JSONDecodeError) as exc:
        raise AdvisorError("local model server returned invalid JSON") from exc
    if not isinstance(envelope, dict):
        raise AdvisorError("local model server response must be a JSON object")

    message = envelope.get("message")
    if not isinstance(message, dict) or not isinstance(message.get("content"), str):
        raise AdvisorError("local model server response is missing message.content")

    try:
        untrusted_advice = json.loads(message["content"])
    except json.JSONDecodeError as exc:
        raise AdvisorError("model message.content is not schema-constrained JSON") from exc
    return validate_advice(untrusted_advice)


def render_kotlin(advice: Mapping[str, Any]) -> str:
    """Render validated advice as a copy-paste AdaptiveFlowGrid call."""

    checked = validate_advice(dict(advice))
    font_scale_aware = str(checked["fontScaleAware"]).lower()
    return "\n".join(
        [
            "AdaptiveFlowGrid(",
            f"    minColumnWidth = {checked['minColumnWidthDp']}.dp,",
            f"    maxColumns = {checked['maxColumns']},",
            f"    horizontalSpacing = {checked['horizontalSpacingDp']}.dp,",
            f"    verticalSpacing = {checked['verticalSpacingDp']}.dp,",
            f"    fontScaleAware = {font_scale_aware},",
            ") {",
            "    // Grid content",
            "}",
        ]
    )


def create_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(
        description=(
            "Ask a local Ollama-compatible model for a validated "
            "AdaptiveFlowGrid configuration."
        )
    )
    parser.add_argument(
        "description",
        help="short description of the grid items and layout goal",
    )
    parser.add_argument(
        "--model",
        default=os.environ.get("KITFLOW_ADVISOR_MODEL", DEFAULT_MODEL),
        help=(
            "already-installed local model name "
            f"(default: $KITFLOW_ADVISOR_MODEL or {DEFAULT_MODEL})"
        ),
    )
    parser.add_argument(
        "--endpoint",
        default=os.environ.get("KITFLOW_ADVISOR_ENDPOINT", DEFAULT_ENDPOINT),
        help=(
            "loopback Ollama-compatible /api/chat URL "
            f"(default: $KITFLOW_ADVISOR_ENDPOINT or {DEFAULT_ENDPOINT})"
        ),
    )
    parser.add_argument(
        "--screen-widths",
        default="320,360,412,600,840",
        metavar="DP,DP,...",
        help="viewport widths to consider in dp (default: %(default)s)",
    )
    parser.add_argument(
        "--font-scales",
        default="1.0,1.3,2.0",
        metavar="SCALE,SCALE,...",
        help="font scales to consider (default: %(default)s)",
    )
    parser.add_argument(
        "--timeout",
        type=float,
        default=60.0,
        metavar="SECONDS",
        help="request timeout greater than 0 and at most 600 seconds (default: %(default)s)",
    )
    return parser


def main(argv: Optional[Sequence[str]] = None) -> int:
    parser = create_parser()
    args = parser.parse_args(argv)

    try:
        endpoint = validate_endpoint(args.endpoint)
        screen_widths = parse_integer_list(
            args.screen_widths,
            option="--screen-widths",
            minimum=1,
            maximum=10_000,
        )
        font_scales = parse_float_list(
            args.font_scales,
            option="--font-scales",
            minimum=0.5,
            maximum=5.0,
        )
        payload = build_request_payload(
            model=args.model,
            description=args.description,
            screen_widths_dp=screen_widths,
            font_scales=font_scales,
        )
        advice = request_advice(
            endpoint=endpoint,
            payload=payload,
            timeout_seconds=args.timeout,
        )
    except AdvisorError as exc:
        parser.exit(2, f"layout-advisor: error: {exc}\n")

    print(render_kotlin(advice))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
