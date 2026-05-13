# Sprint 0 — Planning

## Product Vision

A Spring Boot REST and GraphQL blogging platform API that enables users to create, share, and interact with blog posts securely, with optimized data access, caching, and JWT-based authentication.

---

## Definition of Done (DoD)

A backlog item is **Done** when all of the following are true:

- [ ] Code compiles without errors
- [ ] Feature is implemented and matches the acceptance criteria
- [ ] At least one unit or integration test covers the new functionality
- [ ] All existing tests continue to pass
- [ ] Code is committed with a descriptive message following conventional commits (`feat:`, `fix:`, `perf:`, `chore:`)
- [ ] No sensitive credentials are committed (application config is git-ignored)
- [ ] API changes are reflected in Swagger UI (`/swagger-ui.html`) or GraphiQL
- [ ] Code has been self-reviewed before committing

---

## Product Backlog

| ID | User Story | Priority | Story Points |
|----|------------|----------|-------------|
| US-01 | As a blogger, I want to create a user account so that I can publish and manage my posts. | High | 3 |
| US-02 | As a blogger, I want to create, update, and delete my posts so that I can manage my content. | High | 5 |
| US-03 | As a reader, I want to browse all posts with pagination and sorting so that I can find content efficiently. | High | 3 |
| US-04 | As a reader, I want to search posts by keyword so that I can find relevant content quickly. | Medium | 2 |
| US-05 | As a reader, I want to view a list of popular posts so that I can discover trending content. | Medium | 2 |
| US-06 | As a reader, I want to add comments to posts and read others' comments so that I can engage with the community. | Medium | 3 |
| US-07 | As a reader, I want to submit a star rating and written review for a post so that I can share feedback. | Medium | 3 |
| US-08 | As a power user, I want to query the platform via GraphQL so that I can fetch exactly the data I need. | Low | 5 |
| US-09 | As a registered user, I want to log in with JWT authentication so that my account and data are protected. | High | 8 |
| US-10 | As an administrator, I want all authentication events logged so that I can audit access and detect threats. | Medium | 3 |

**Total estimated points:** 37

---

## Acceptance Criteria

### US-01 — User Registration
- `POST /api/users` returns `200` with a `UserDTO` body wrapped in `ApiResponse`
- Username and email must be unique (validated via `@UniqueUsername` and `@UniqueEmail`)
- Missing or invalid fields return a `400` error with a descriptive message
- Password is not returned in the response DTO

### US-02 — Post Management (CRUD)
- `POST /api/posts` creates a post and returns the created `PostDTO`
- `PUT /api/posts/{id}` updates an existing post; returns `404` if not found
- `DELETE /api/posts/{id}` removes the post; returns `200` with success message
- All write operations are wrapped in a `@Transactional` boundary

### US-03 — Paginated Post Browsing
- `GET /api/posts?page=0&size=10&sort=createdAt,desc` returns a `Page<PostDTO>`
- Sort direction (`asc`/`desc`) and field are configurable via query params
- Default page size is 10; default sort is `createdAt` descending

### US-04 — Post Search
- `GET /api/posts/search?keyword=spring` returns a list of posts matching the keyword
- Search is case-insensitive and matches post titles or body content

### US-05 — Popular Posts
- `GET /api/posts/popular?limit=5` returns the top N posts
- Results are cached with Spring Cache (`@Cacheable`) to reduce database load
- Cache is invalidated when new posts are created or updated (`@CacheEvict`)

### US-06 — Comments
- `POST /api/comments` adds a comment linked to a post; returns `CommentDTO`
- `GET /api/posts/{postId}/comments?page=0&size=10` returns paginated comments for a post
- Comment body must not be blank

### US-07 — Reviews
- `POST /api/reviews` adds a review with a numeric rating linked to a post; returns `ReviewDTO`
- `GET /api/posts/{postId}/reviews?page=0&size=10` returns paginated reviews
- Rating must be between 1 and 5

### US-08 — GraphQL API
- A GraphQL endpoint is available at `/graphql`; GraphiQL UI at `/graphiql`
- Queries for posts, comments, and reviews are supported
- Responses return only the fields requested by the client

### US-09 — JWT Authentication
- `POST /api/auth/login` accepts a `LoginRequest` (username + password) and returns a signed JWT
- Protected endpoints reject requests without a valid `Authorization: Bearer <token>` header
- Tokens are added to a blacklist on logout, preventing reuse

### US-10 — Security Event Logging
- `SecurityLoggingAspect` intercepts auth-related calls and logs the event with timestamp and principal
- Failed login attempts are logged at `WARN` level
- Successful logins are logged at `INFO` level

---

## Sprint 1 Plan

**Sprint goal:** Deliver the core data model and a fully functional REST + GraphQL API with pagination, caching, and validation.

| Story | Points |
|-------|--------|
| US-01 — User Registration | 3 |
| US-02 — Post Management (CRUD) | 5 |
| US-03 — Paginated Post Browsing | 3 |
| US-04 — Post Search | 2 |
| US-05 — Popular Posts (cached) | 2 |
| US-06 — Comments | 3 |
| US-07 — Reviews | 3 |
| US-08 — GraphQL API | 5 |

**Sprint 1 total:** 26 points

---

## Sprint 2 Plan

**Sprint goal:** Harden the platform with JWT authentication, security logging, and observability.

| Story | Points |
|-------|--------|
| US-09 — JWT Authentication | 8 |
| US-10 — Security Event Logging | 3 |
| Monitoring — Add `/actuator/health` endpoint | (non-story task) |

**Sprint 2 total:** 11 points + monitoring task
