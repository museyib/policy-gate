# Policy Gate

Policy Gate is a Spring Boot REST API for account authentication and resource-based access control. It uses PostgreSQL for persistence, Flyway for schema migrations, JWT access tokens, and refresh tokens.

## Technology

- Java 25
- Spring Boot 4.1.2
- Spring MVC, Spring Security, Spring Data JPA, and Jakarta Validation
- PostgreSQL and Flyway
- JJWT for token handling
- MapStruct and Lombok
- Springdoc OpenAPI / Swagger UI
- Maven Wrapper

## Requirements

- JDK 25
- PostgreSQL

Create a PostgreSQL database named `policy_gate`, or configure another database with the environment variables below. The default local connection is `jdbc:postgresql://localhost:5432/policy_gate` with username and password `postgres`.

## Run locally

Set configuration as environment variables if you do not want to use the local defaults, then start the API:

```bash
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd spring-boot:run`.

Flyway applies migrations from `src/main/resources/db/migration` at startup. Hibernate validates the resulting schema; it does not create or update tables. To package the application:

```bash
./mvnw clean package
```

Run the test suite with:

```bash
./mvnw test
```

The API listens on port 8080 by default.

## Configuration

All settings are in `src/main/resources/application.yaml`. Environment variables override the listed local defaults.

| Variable | Default | Purpose |
| --- | --- | --- |
| `DATABASE_URL` | `jdbc:postgresql://localhost:5432/policy_gate` | JDBC connection URL |
| `DATABASE_USERNAME` | `postgres` | Database user |
| `DATABASE_PASSWORD` | `postgres` | Database password |
| `JWT_ISSUER` | `policy-gate` | JWT issuer claim |
| `JWT_SECRET` | Local development key | JWT signing key |
| `APP_JWT_ACCESS_TTL_MINUTES` | `15` | Access token lifetime |
| `APP_JWT_REFRESH_TTL_DAYS` | `14` | Refresh token lifetime |
| `BOOTSTRAP_ADMIN_EMAIL` | `admin@example.com` | Initial administrator email |
| `BOOTSTRAP_ADMIN_PASSWORD` | `AdminPas123456` | Initial administrator password |
| `BOOTSTRAP_ADMIN_DISPLAY_NAME` | `Bootstrap Admin` | Initial administrator display name |
| `MAX_PAGE_SIZE` | `100` | Maximum configured page size |

The bootstrap administrator is created only when its email does not already exist and both email and password are non-empty. Replace the local defaults, especially the bootstrap password and JWT secret, before deploying. Configure `JWT_SECRET` with a strong secret suitable for the application's signing key.

## API reference

Interactive API documentation is available at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html). The OpenAPI document is at [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs).

### Authentication

| Method | Path | Purpose |
| --- | --- | --- |
| `POST` | `/api/auth/register` | Register an account |
| `POST` | `/api/auth/login` | Authenticate and obtain tokens |
| `POST` | `/api/auth/refresh` | Refresh tokens |
| `POST` | `/api/auth/logout` | Revoke a refresh token |

Send the access token on protected requests as `Authorization: Bearer <access-token>`.

### Users

| Method | Path | Required permission |
| --- | --- | --- |
| `GET` | `/api/users` | `USER:READ` |
| `GET` | `/api/users/{id}` | `USER:READ` |
| `PATCH` | `/api/users/{id}` | `USER:UPDATE` |

### Roles

| Method | Path | Required permission |
| --- | --- | --- |
| `GET` | `/api/roles` | `ROLE:READ` |
| `GET` | `/api/roles/{id}` | `ROLE:READ` |
| `POST` | `/api/roles` | `ROLE:CREATE` |
| `PATCH` | `/api/roles/{id}` | `ROLE:UPDATE` |
| `DELETE` | `/api/roles/{id}` | `ROLE:DELETE` |

### Resources

| Method | Path | Required permission |
| --- | --- | --- |
| `GET` | `/api/resources` | `RESOURCE:READ` |
| `GET` | `/api/resources/{id}` | `RESOURCE:READ` |
| `POST` | `/api/resources` | `RESOURCE:CREATE` |
| `PATCH` | `/api/resources/{id}` | `RESOURCE:UPDATE` |
| `DELETE` | `/api/resources/{id}` | `RESOURCE:DELETE` |

### Permission assignments

| Method | Path | Required permission |
| --- | --- | --- |
| `GET` | `/api/permissions/role/{roleId}` | `PERMISSION:READ` |
| `POST` | `/api/permissions/role/{roleId}` | `PERMISSION:UPDATE` |
| `GET` | `/api/permissions/user/{userId}` | `PERMISSION:READ` |
| `POST` | `/api/permissions/user/{userId}` | `PERMISSION:UPDATE` |

The security layer checks resource/action permissions for protected operations. The initial migration seeds a `View Users` role, the `USER`, `ROLE`, `RESOURCE`, and `PERMISSION` resources, and a `USER:READ` permission for that role.

## Project layout

```text
src/main/java/llc/devlab/app/
├── config/                 Application configuration and OpenAPI setup
├── exception/              API exceptions and centralized error responses
├── model/                  Shared persistence model and DTOs
├── security/
│   ├── config/             HTTP and method security, JWT properties
│   ├── controller/         Authentication and access-control endpoints
│   ├── filter/             JWT authentication filter
│   ├── mapper/             DTO/entity mapping
│   ├── model/              Security entities, DTOs, and enums
│   ├── repository/         Persistence interfaces
│   └── service/            Authentication, token, and permission logic
└── PolicyGateApplication.java
src/main/resources/
├── application.yaml        Runtime configuration
└── db/migration/           Versioned Flyway SQL migrations
src/test/java/              Automated tests
```

## Request workflow

1. On startup, Flyway creates and seeds the PostgreSQL schema, then JPA validates the mappings.
2. The bootstrap initializer creates the configured administrator if it is not already present.
3. A client registers or logs in through `/api/auth`; the response contains authentication tokens.
4. The client sends the access token as a bearer token to protected endpoints. The JWT filter authenticates the request, and method security checks the caller's resource/action permission.
5. When the access token expires, the client can exchange its refresh token at `/api/auth/refresh`; `/api/auth/logout` revokes a refresh token.

## Database migrations

Migrations are applied in version order and should be added as new files following Flyway's `V<number>__description.sql` naming convention. Existing migrations:

- `V1__init_schema.sql` creates users, roles, resources, permissions, refresh-token, and relationship tables.
- `V2__seed_data.sql` adds the initial role, resources, and user-read permission.
