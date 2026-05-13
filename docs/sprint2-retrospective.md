# Sprint 2 — Final Retrospective

**Sprint dates:** May 3, 2026

---

## What Went Well

- **Process improvements landed:** Both action items from the Sprint 1 Retrospective (tests and CI) were implemented in Sprint 2. The pipeline failing on a bad commit in Sprint 2 caught a test regression before it reached `main` — exactly the outcome the improvement was designed for.
- **Security was additive, not disruptive:** JWT authentication was bolted on top of the existing service and controller layers without modifying the business logic. The AOP-based security logging had zero impact on existing controller code.
- **Incremental commits remained consistent:** Sprint 2 commits followed the same conventional-commit convention established in Sprint 1, keeping the history clean and readable.
- **Monitoring is lightweight and production-ready:** Spring Boot Actuator provides health checks with database connectivity verification, which would integrate directly into any deployment health check (Docker, Kubernetes, etc.).

---

## What Could Be Improved

### Improvement 1 — Test coverage was added late

Tests were added as a sprint 2 task rather than alongside the features they test. This means Sprint 1 features lived untested for the entire first sprint. In a real project, this would be a risk — a regression in US-01 (user creation) would not be detected until Sprint 2.

**Lesson:** The Definition of Done already requires tests per story. Enforcing this strictly from Sprint 0 would have caught this gap earlier.

### Improvement 2 — Token blacklist is in-memory only

`TokenBlacklistService` stores invalidated tokens in a `HashSet`. This works in a single-instance deployment but would not survive a server restart or scale to multiple instances.

**Lesson:** For a production system, token blacklisting should be backed by a shared store (e.g., Redis). This was acceptable for the prototype scope but should be listed as tech debt in the backlog.

### Improvement 3 — No end-to-end or integration tests

Unit tests cover service-layer logic, but there are no tests that exercise the full HTTP request lifecycle (controller → service → repository → database). A failing database migration or misconfigured security filter would not be caught by unit tests alone.

**Lesson:** Spring Boot's `@SpringBootTest` with a test database (H2 or Testcontainers) should be added in the next sprint to provide an integration test baseline.

---

## Key Lessons Learned

1. **Commit discipline pays off.** The 12-commit history, one logical change per commit, made it trivial to map each story to specific code changes. This would make rollbacks, bisecting bugs, and code review significantly easier on a real team.

2. **CI is a forcing function.** Once the pipeline was in place, the temptation to skip tests or commit broken code disappeared — the pipeline makes the cost of cutting corners immediate and visible.

3. **Agile ceremonies produce real artifacts.** Writing sprint reviews and retrospectives forced a deliberate look at what was skipped (tests in Sprint 1) rather than glossing over it. The retrospective turned a gap into an action item that was actually addressed.

4. **Security is easier to design in early.** JWT authentication required changes to `SecurityConfig` but no changes to business logic because the layered architecture kept concerns separate. Starting with security in mind would have made even the config step smaller.
