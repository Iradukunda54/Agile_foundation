# Sprint 1 Review

**Sprint dates:** April 27–28, 2026  
**Sprint goal:** Deliver the core data model and a fully functional REST + GraphQL API with pagination, caching, and validation.

---

## Delivered Items

All 8 stories planned for Sprint 1 were completed.

| Story | Status | Commits |
|-------|--------|---------|
| US-01 — User Registration | Done | `c27a73e`, `521bdef`, `49cb161` |
| US-02 — Post Management (CRUD) | Done | `c27a73e`, `aab38a0`, `521bdef`, `49cb161` |
| US-03 — Paginated Post Browsing | Done | `aab38a0`, `521bdef`, `49cb161`, `3bc6d5d` |
| US-04 — Post Search | Done | `aab38a0`, `521bdef` |
| US-05 — Popular Posts (cached) | Done | `3bc6d5d` |
| US-06 — Comments | Done | `c27a73e`, `aab38a0`, `521bdef`, `49cb161` |
| US-07 — Reviews | Done | `c27a73e`, `aab38a0`, `521bdef`, `49cb161` |
| US-08 — GraphQL API | Done | `49cb161` |

---

## What Was Built

### Data Model (`868dec3`, `c27a73e`)
Five JPA entities were defined and mapped to PostgreSQL:
- `User` — username, email, password, role
- `Post` — title, body, tags, author (ManyToOne User)
- `Comment` — body, post (ManyToOne Post), author (ManyToOne User)
- `Review` — rating (1–5), text, post (ManyToOne Post)
- `Tag` — name, many-to-many with Post

### Repository Layer (`aab38a0`)
Spring Data JPA repositories created for all entities with:
- Derived queries (`findByAuthor_Username`, `findByTitleContainingIgnoreCase`)
- Custom JPQL queries for popularity ranking
- Pageable support across Post, Comment, and Review repositories

### Service Layer (`521bdef`)
Service interfaces and `@Transactional` implementations for:
- `UserService` / `UserServiceImpl` — CRUD with DTO mapping
- `PostService` / `PostServiceImpl` — CRUD, search, popular posts
- `CommentService` / `CommentServiceImpl` — add and fetch by post
- `ReviewService` / `ReviewServiceImpl` — add and fetch by post

### REST API + Caching (`49cb161`, `3bc6d5d`)
Three REST controllers exposed:
- `POST /api/users` · `GET /api/users/{id}` · `GET /api/users` · `PUT /api/users/{id}` · `DELETE /api/users/{id}`
- `POST /api/posts` · `GET /api/posts` · `GET /api/posts/{id}` · `GET /api/posts/search` · `GET /api/posts/popular` · `GET /api/posts/author/{username}` · `PUT /api/posts/{id}` · `DELETE /api/posts/{id}`
- `POST /api/comments` · `GET /api/posts/{id}/comments` · `POST /api/reviews` · `GET /api/posts/{id}/reviews`

Spring Cache (`@Cacheable`, `@CacheEvict`) applied to popular posts. `GlobalExceptionHandler` returns structured error responses.

### GraphQL API (`49cb161`)
`BlogGraphQlController` exposes queries for posts, comments, and reviews. Available at `/graphql` with GraphiQL explorer at `/graphiql`.

### OpenAPI / Swagger (`49cb161`)
`OpenApiConfig` configures Springdoc; full API documentation available at `/swagger-ui.html`.

### Security Baseline (`664df82`)
Sensitive config (`application-dev.yml`) removed from tracking; `.gitignore` updated.

---

## Demo

| Endpoint | Result |
|----------|--------|
| `GET /swagger-ui.html` | All REST endpoints visible and testable |
| `GET /graphiql` | GraphQL explorer available |
| `GET /api/posts?page=0&size=5&sort=createdAt,desc` | Returns first page of posts sorted by date |
| `GET /api/posts/popular?limit=3` | Returns 3 popular posts (cached on second call) |
| `GET /api/posts/search?keyword=spring` | Returns posts matching keyword |

---

## What Was Not Completed

- Authentication (JWT) — deferred to Sprint 2
- Unit tests — not yet written; identified as a process improvement for Sprint 2
- CI/CD pipeline — not configured; identified as a process improvement for Sprint 2
