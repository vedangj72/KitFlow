"""Tests for the dependency-free KitFlow layout advisor."""

from __future__ import annotations

import json
import threading
import unittest
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from typing import Any

import layout_advisor


VALID_ADVICE = {
    "minColumnWidthDp": 240,
    "maxColumns": 4,
    "horizontalSpacingDp": 12,
    "verticalSpacingDp": 12,
    "fontScaleAware": True,
}


class ValidationTests(unittest.TestCase):
    def test_valid_advice_renders_copy_paste_kotlin(self) -> None:
        self.assertEqual(
            layout_advisor.render_kotlin(VALID_ADVICE),
            """AdaptiveFlowGrid(
    minColumnWidth = 240.dp,
    maxColumns = 4,
    horizontalSpacing = 12.dp,
    verticalSpacing = 12.dp,
    fontScaleAware = true,
) {
    // Grid content
}""",
        )

    def test_rejects_missing_extra_wrong_type_and_out_of_range_fields(self) -> None:
        invalid_values = [
            {key: value for key, value in VALID_ADVICE.items() if key != "maxColumns"},
            {**VALID_ADVICE, "rationale": "untrusted extra output"},
            {**VALID_ADVICE, "maxColumns": True},
            {**VALID_ADVICE, "minColumnWidthDp": 47},
            {**VALID_ADVICE, "horizontalSpacingDp": 65},
            {**VALID_ADVICE, "fontScaleAware": "true"},
        ]

        for value in invalid_values:
            with self.subTest(value=value), self.assertRaises(layout_advisor.AdvisorError):
                layout_advisor.validate_advice(value)

    def test_endpoint_is_restricted_to_loopback_api_chat(self) -> None:
        accepted = [
            "http://127.0.0.1:11434/api/chat",
            "http://127.2.3.4:8080/api/chat/",
            "http://[::1]:11434/api/chat",
        ]
        rejected = [
            "http://localhost:11434/api/chat",
            "http://127.0.0.1.:11434/api/chat",
            "http://example.com:11434/api/chat",
            "http://192.168.1.10:11434/api/chat",
            "http://localhost:11434/api/generate",
            "file:///api/chat",
            "http://localhost/api/chat",
            "http://user:secret@localhost:11434/api/chat",
        ]

        for endpoint in accepted:
            with self.subTest(endpoint=endpoint):
                self.assertEqual(layout_advisor.validate_endpoint(endpoint), endpoint)
        for endpoint in rejected:
            with self.subTest(endpoint=endpoint), self.assertRaises(
                layout_advisor.AdvisorError
            ):
                layout_advisor.validate_endpoint(endpoint)


class RequestTests(unittest.TestCase):
    def setUp(self) -> None:
        self.received: dict[str, Any] = {}
        test_case = self

        class Handler(BaseHTTPRequestHandler):
            def do_POST(self) -> None:  # noqa: N802 - stdlib callback name
                length = int(self.headers["Content-Length"])
                test_case.received["path"] = self.path
                test_case.received["payload"] = json.loads(
                    self.rfile.read(length).decode("utf-8")
                )
                response_body = json.dumps(
                    {"message": {"role": "assistant", "content": json.dumps(VALID_ADVICE)}}
                ).encode("utf-8")
                self.send_response(200)
                self.send_header("Content-Type", "application/json")
                self.send_header("Content-Length", str(len(response_body)))
                self.end_headers()
                self.wfile.write(response_body)

            def log_message(self, format: str, *args: object) -> None:
                pass

        self.server = ThreadingHTTPServer(("127.0.0.1", 0), Handler)
        self.thread = threading.Thread(target=self.server.serve_forever, daemon=True)
        self.thread.start()

    def tearDown(self) -> None:
        self.server.shutdown()
        self.server.server_close()
        self.thread.join(timeout=2)

    def test_request_uses_chat_json_schema_and_validates_content(self) -> None:
        payload = layout_advisor.build_request_payload(
            model="test-model",
            description="Product cards with an image, title, and two actions",
            screen_widths_dp=[320, 600],
            font_scales=[1.0, 2.0],
        )
        endpoint = f"http://127.0.0.1:{self.server.server_port}/api/chat"

        result = layout_advisor.request_advice(
            endpoint=endpoint,
            payload=payload,
            timeout_seconds=2,
        )

        self.assertEqual(result, VALID_ADVICE)
        self.assertEqual(self.received["path"], "/api/chat")
        sent = self.received["payload"]
        self.assertFalse(sent["stream"])
        self.assertEqual(sent["format"], layout_advisor.ADVICE_SCHEMA)
        self.assertEqual(sent["format"]["additionalProperties"], False)
        self.assertEqual(sent["options"]["temperature"], 0)


if __name__ == "__main__":
    unittest.main()
