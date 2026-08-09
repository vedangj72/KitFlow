---
name: gpt
description: Use when working on KitFlow SDK tasks that should be handled with GPT-specific prompting, reasoning, or response style in the shared library.
---

# GPT

Use this skill for work in the main KitFlow SDK, especially under `shared/`.

## When to use

- Writing or refining SDK-facing prompts
- Shaping GPT-oriented responses or workflows
- Updating shared library code that should keep GPT-specific behavior isolated from the sample app

## Guidance

- Prefer changes in `shared/src/commonMain` unless a platform target is required.
- Keep prompts concise and specific to the SDK API surface.
- Avoid placing GPT-only behavior in `manual-testing/`.
- If a task touches public SDK docs or examples, keep them aligned with the shared module behavior.

