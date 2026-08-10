---
name: gemini
description: Use when working on KitFlow SDK tasks that should be handled with Gemini-specific prompting, reasoning, or response style in the shared library.
---

# Gemini

Use this skill for work in the main KitFlow SDK, especially under `shared/`.

## When to use

- Writing or refining SDK-facing prompts
- Shaping Gemini-oriented responses or workflows
- Updating shared library code that should keep Gemini-specific behavior isolated from the sample app

## Guidance

- Prefer changes in `shared/src/commonMain` unless a platform target is required.
- Keep prompts concise and specific to the SDK API surface.
- Avoid placing Gemini-only behavior in `manual-testing/`.
- If a task touches public SDK docs or examples, keep them aligned with the shared module behavior.
