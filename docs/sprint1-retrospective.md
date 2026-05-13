# Sprint 1 Retrospective

**Sprint dates:** April 27–28, 2026

---

## What Went Well

- **Incremental delivery:** Each layer of the application was committed separately — entities first, then repositories, then services, then controllers. This made the history easy to follow and each commit deployable in isolation.
- **API completeness:** All planned REST endpoints were delivered and documented via Swagger UI within the sprint.
- **Separation of concerns:** The service/repository/controller layering made the codebase straightforward to extend and reason about.
- **Sensitive config handled correctly:** Database credentials were kept out of version control from the start using `.gitignore`.

---

## What Could Be Improved

### Improvement 1 — Add unit and integration tests

**Problem:** Sprint 1 delivered no tests. If a refactor or new feature introduces a regression, there is no safety net to catch it before it is committed.

**Action for Sprint 2:** Write JUnit 5 unit tests for the service layer and at least one integration test for the REST endpoints. Tests must pass before any story is marked Done (per the Definition of Done).

### Improvement 2 — Set up a CI pipeline

**Problem:** There is no automated check running on each push. Bad builds or failing tests could be merged to `main` undetected.

**Action for Sprint 2:** Add a GitHub Actions workflow (`main.yml`) that runs `mvn test` on every push and pull request to `main`. The pipeline must be green before any Sprint 2 work is considered complete.

---

## Action Items for Sprint 2

| # | Action | Owner |
|---|--------|-------|
| 1 | Write unit tests for `PostServiceImpl`, `CommentServiceImpl`, `UserServiceImpl` | Dev |
| 2 | Configure GitHub Actions CI pipeline with `mvn test` | Dev |
| 3 | Add Spring Boot Actuator for `/actuator/health` monitoring endpoint | Dev |
