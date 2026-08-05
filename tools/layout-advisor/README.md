# KitFlow local layout advisor

This optional development tool asks an **already running local**
Ollama-compatible model to suggest parameters for KitFlow's
`AdaptiveFlowGrid` API. It then validates the response and prints a copy-paste
Kotlin snippet.

It is deliberately separate from `shared`:

- it adds no KitFlow runtime or library dependency;
- it uses only the Python standard library;
- it never downloads, bundles, or starts a model; and
- it is not part of the Gradle build or published artifact.

## Prerequisites

- Python 3.9 or newer
- An Ollama-compatible server listening on a loopback address
- A small instruction model that you installed yourself

For example, start Ollama and make sure the model named by `--model` is already
available. The advisor does **not** run `ollama pull` or otherwise fetch weights.
The default model name is `qwen2.5:0.5b`; use any already-installed model that
reliably supports Ollama structured output.

Ollama documents the [local API](https://docs.ollama.com/api/introduction),
[`/api/chat`](https://docs.ollama.com/api/chat), and
[structured outputs](https://docs.ollama.com/capabilities/structured-outputs).

## Run

From the repository root:

```powershell
python tools/layout-advisor/layout_advisor.py `
  "Product cards with a 16:9 image, a two-line title, and two actions" `
  --screen-widths 320,360,412,600,840 `
  --font-scales 1.0,1.3,2.0 `
  --model qwen2.5:0.5b
```

On shells that do not use PowerShell continuation characters:

```sh
python tools/layout-advisor/layout_advisor.py \
  "Product cards with a 16:9 image, a two-line title, and two actions" \
  --screen-widths 320,360,412,600,840 \
  --font-scales 1.0,1.3,2.0 \
  --model qwen2.5:0.5b
```

Example output:

```kotlin
AdaptiveFlowGrid(
    minColumnWidth = 240.dp,
    maxColumns = 4,
    horizontalSpacing = 12.dp,
    verticalSpacing = 12.dp,
    fontScaleAware = true,
) {
    // Grid content
}
```

Options:

- `--endpoint` selects the Ollama-compatible chat endpoint. It defaults to
  `http://127.0.0.1:11434/api/chat` and must use a numeric loopback address,
  an explicit port, and the `/api/chat` path.
- `--model` selects an already-installed local model.
- `--screen-widths` supplies comma-separated viewport widths in dp.
- `--font-scales` supplies comma-separated accessibility font scales.
- `--timeout` sets a request timeout of at most 600 seconds.

`KITFLOW_ADVISOR_ENDPOINT` and `KITFLOW_ADVISOR_MODEL` can provide the endpoint
and model defaults. Explicit command-line options override them.

The Kotlin output uses Compose `Dp` literals, so the destination source file
must have `import androidx.compose.ui.unit.dp` (as most Compose layout files do).

## Response contract

The request puts a JSON Schema in Ollama's `format` field, disables streaming,
and sets temperature to zero. The only accepted model result is an object with
exactly these fields:

```json
{
  "minColumnWidthDp": 240,
  "maxColumns": 4,
  "horizontalSpacingDp": 12,
  "verticalSpacingDp": 12,
  "fontScaleAware": true
}
```

The tool validates required fields, rejects extra fields and wrong JSON types,
and enforces conservative numeric ranges before emitting Kotlin. Treat the
result as a design suggestion: preview it at the supplied widths and font scales
and review it before committing code.

## Security and privacy

The endpoint option intentionally accepts only numeric addresses in the
`127.0.0.0/8` loopback range or `::1`; hostnames, remote addresses, and LAN
addresses are rejected, and HTTP redirects and system HTTP proxies are not
followed. This reduces the risk of accidentally sending an unreleased layout
description over the network.
However, a local process, proxy, or model-server configuration can still log or
forward prompts. Do not include secrets, credentials, personal data, customer
content, or proprietary source code. Review and secure the model server
separately, and never execute model output blindly.

## Test

No live model is needed. The tests use an in-process loopback HTTP server:

```powershell
python -m unittest discover -s tools/layout-advisor -p "test_*.py" -v
```
