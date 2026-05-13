# Sprint 2 Review

**Sprint dates:** May 3, 2026  
**Sprint goal:** Harden the platform with JWT authentication, security logging, and observability; apply process improvements identified in Sprint 1 retrospective.

---

## Delivered Items

| Story / Task | Status | Commits |
|---|---|---|
| US-09 — JWT Authentication | Done | `60fc92a`, `0bce77c`, `584748d`, `09c66a1` |
| US-10 — Security Event Logging | Done | `dbf3372` |
| Monitoring — Spring Boot Actuator health endpoint | Done | (pom.xml + config) |
| Process — CI/CD GitHub Actions pipeline | Done | `.github/workflows/main.yml` |
| Process — Unit tests added | Done | `src/test/` |

---

## What Was Built

### JWT Authentication (`60fc92a`, `0bce77c`, `09c66a1`)

- **`JwtUtils`** — generates signed JWT tokens with configurable expiry; validates tokens on incoming requests using HMAC-SHA256.
- **`LoginRequest` DTO** — accepts `username` and `password`; used by the login endpoint.
- **`TokenBlacklistService`** — maintains an in-memory set of invalidated tokens. On logout, the token is added to the blacklist; `JwtUtils` checks the blacklist on every validation call, preventing reuse after logout.

### Security Configuration (`584748d`)

`SecurityConfig` defines the Spring Security filter chain:
- Public endpoints: `POST /api/users` (registration), `POST /api/auth/login`
- All other endpoints require a valid `Authorization: Bearer <token>` header
- JWT filter inserted before `UsernamePasswordAuthenticationFilter`
- CSRF disabled (stateless REST API)

### Security Event Logging (`dbf3372`)

`SecurityLoggingAspect` is an AOP aspect that intercepts authentication operations:
- Logs `INFO` for successful logins with the principal name and timestamp
- Logs `WARN` for failed login attempts with the reason
- All log entries include a correlation marker for traceability

### Spring Boot Actuator — Health Endpoint

`/actuator/health` returns the application health status and database connectivity check:

```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "diskSpace": { "status": "UP" }
  }
}
```

Only the `health` endpoint is exposed publicly; all other actuator endpoints are secured.

---

## Process Improvements Applied (from Sprint 1 Retrospective)

| Improvement | Applied? |
|---|---|
| Write unit tests for service layer | Yes — JUnit 5 tests added for `PostServiceImpl` and `UserServiceImpl` |
| Configure GitHub Actions CI pipeline | Yes — `main.yml` runs `mvn test` on every push |
| Add monitoring endpoint | Yes — Actuator health endpoint live at `/actuator/health` |

---

## Demo

| Endpoint / Item | Result |
|---|---|
| `POST /api/auth/login` | Returns signed JWT token |
| `GET /api/posts` without token | Returns `401 Unauthorized` |
| `GET /api/posts` with valid token | Returns paginated posts |
| `GET /actuator/health` | Returns `{"status":"UP"}` |
| GitHub Actions pipeline | Passes `mvn test` on push to `main` |
| GraphiQL at `/graphiql` | Still available; secured endpoints require token |
