---
name: claude
description: Use when working on KitFlow SDK tasks that should be handled with Claude-specific prompting, reasoning, or response style in the shared library.
---

# Claude

Use this skill for work in the main KitFlow SDK, especially under `shared/`.

## When to use

- Writing or refining SDK-facing prompts
- Shaping Claude-oriented responses or workflows
- Updating shared library code that should keep Claude-specific behavior isolated from the sample app

## Guidance

- Prefer changes in `shared/src/commonMain` unless a platform target is required.
- Keep prompts concise and specific to the SDK API surface.
- Avoid placing Claude-only behavior in `manual-testing/`.
- If a task touches public SDK docs or examples, keep them aligned with the shared module behavior.

