---
name: Spring-GraphQL workspace instructions
description: "Workspace-level guidance for AI assistants working on the Spring-GraphQL repository (backend: Java/Spring Boot; frontend: Angular). Use when: running builds/tests, editing backend or frontend code, scaffolding features, or diagnosing CI issues."
applyTo:
  - "back/**"
  - "front/**"
---

Always respond and explain your reasoning in French.

# Spring-GraphQL — Agent Instructions

Short summary: this repository contains a Spring Boot GraphQL backend and an Angular frontend. Prefer small, reviewable changes and link to existing docs rather than duplicating them.

**Project layout (high level):**
- Backend: `back/` — Maven Spring Boot app (Java 24)
- Frontend: `front` — Angular 21 app (npm)

**Where to look first:**
- Repository README: [README.md](README.md)
- Backend build config: `back/pom.xml`
- Frontend scripts: `front/package.json`

**Build & run (use these exact commands when giving run instructions):**

- Backend (from repo root):
  - Build: `mvn -f back clean package`
  - Run: `cd back && mvn spring-boot:run`  (or `mvn -f back spring-boot:run`)
  - Tests: `mvn -f back test`

- Frontend (from repo root):
  - Install: `npm --prefix front install`
  - Start dev server: `npm --prefix front start`
  - Build: `npm --prefix front run build`
  - Test: `npm --prefix front test`

**Environment notes and preferences:**
- Backend targets Java 24 (see `back/pom.xml`). Use a matching JDK when running or building locally.
- Frontend uses `npm@11.11.0` (see `front/package.json`). Use the workspace package manager when possible.
- Follow the repository's existing style and small-PR workflow: make minimal changes, include tests for logic changes, and avoid broad formatting churn.

**Agent behavior preferences (how you should act):**
- Link, don't embed: point to existing docs and files rather than copying large sections.
- When suggesting code changes, include a concise rationale and unit or integration test suggestions.
- Prefer `applyTo`-scoped changes: if a suggestion only affects backend files, keep edits limited to `back/**`.
- Avoid large dependency upgrades without tests and a changelog note.

**Anti-patterns (do not do):**
- Do not inject broad, repository-wide instructions with `applyTo: "**"` unless the instruction truly applies to all files.
- Do not change build toolchain versions (Java, Node) without explaining why and listing verification steps.

**Example prompts (copy-paste to start):**
- "Run the backend tests and summarize failures, showing failing stack traces and suggested fixes."
- "Add a new GraphQL query to return all teams; update the backend resolver and add a unit test."
- "Scaffold an Angular component `team-list` in `front` and wire it to the GraphQL query."

**Next recommended agent customizations:**
- Create a file-level instruction for `back/src/main/java/**` that documents preferred logging, error-handling patterns, and test style.
- Add a `prompts/` example set for common dev tasks (run tests, create feature scaffold, update API schema).

If you'd like, I can now:
- add this file to the repo (recommended),
- create a smaller `back`-scoped instruction covering Java-specific conventions, or
- generate example prompts saved under `.github/prompts/` for reviewer use.
