---
name: spring-boot-api-starter
description: Create a Java Spring Boot REST API with reusable Maven, persistence, security, validation, error handling, mapping, and API documentation setup tailored to entities defined for each project.
metadata:
  short-description: Create a configurable Spring Boot REST API
---

# Spring Boot API Starter

Create a REST API using the domain entities and business rules the user defines. Keep the platform setup reusable; do
not infer domain requirements from a sample, repository name, or this skill. Domain entities, workflows, authorization
rules, and lifecycle policies vary by project. Use generic placeholders such as `<Entity>` and `<RelatedEntity>` in
plans and examples.

## Define the project

Before implementation, establish details that materially affect the model or API:

- Project name, Maven coordinates, Java package, and required Java/Spring Boot versions.
- Domain entities, fields, constraints, relationships, and cardinality.
- Required operations and which resources need pagination, filtering, sorting, ownership, soft deletion, or other
  business rules.
- Authentication and authorization needs. Use the JWT setup below by default unless the user requests another model.

Use conventional assumptions for incidental details and document them in the README. Do not invent consequential
business behavior. Keep entity names and examples specific to the project's stated domain, and never require particular
entities such as Product or Order.

## Common platform setup

Unless the user requests a smaller or different setup, use:

- Java, Spring Boot, and Maven (include Maven Wrapper for a new project).
- Spring MVC, Spring Data JPA, Bean Validation, and Lombok where useful.
- PostgreSQL at runtime and Flyway versioned migrations as the schema source of truth. Disable Hibernate schema
  creation/update; align JPA mappings and migrations.
- JWT authentication when requested or accepted as the default: registration, login, short-lived access tokens, refresh
  rotation/revocation, BCrypt password hashing, and protected routes. Keep secrets in environment-based configuration.
- Centralized API error handling with a consistent response structure. Validation errors identify invalid fields without
  exposing stack traces, SQL, tokens, or internal details.
- Springdoc OpenAPI/Swagger UI, with documentation endpoints explicitly allowed when security is enabled.
- Consistent pagination metadata (page number, page size, total elements, total pages, `first` and `last`) and a
  configured maximum page size.
- JPA auditing for timestamps and, where useful, actor identity.
- H2 and Spring Boot test dependencies when an isolated test database is appropriate.
- Environment-overridable database and security settings plus README setup and API documentation.

Choose dependency versions from compatible Spring Boot dependency management and official project documentation when
creating a project. Do not pin a version already managed by the Boot parent without a reason. For an existing project,
inspect `pom.xml` and preserve established versions unless asked to upgrade. Add dependencies only for features actually
used; keep compile-time libraries such as mapping APIs distinct from annotation processors, and use appropriate scopes
for database drivers, runtime implementations, and test libraries. Verify Maven's effective dependency graph if
compatibility or duplication is in question.

## Structure and responsibility

Use a clear layered structure, adapted to the project:

```text
<package>/
  controller/       HTTP endpoints and transport concerns
  service/          business rules and transaction boundaries
  repository/       persistence queries
  mapper/           entity and DTO conversion
  model/entity/     JPA entities
  model/dto/        request and response models
  exception/        domain errors and global API handler
  security/         authentication, authorization, and configuration
```

Keep request/response DTOs separate from JPA entities. Never serialize password hashes, refresh-token internals, or
persistence-only fields. Controllers handle HTTP concerns; services enforce business rules and access checks;
repositories encapsulate persistence queries.

## Entity, mapping, and persistence rules

For each requested entity:

1. Define fields, types, nullability, lengths, uniqueness, defaults, indexes, and API exposure from the requirements.
2. Define relationships and foreign keys from stated cardinality. Choose owning sides, join tables, cascade behavior,
   and deletion semantics deliberately. Do not cascade deletion to related records by default.
3. Add validated input DTOs and output DTOs. Use MapStruct when it improves clarity, or explicit mapping for simple
   cases. For update mappings, prevent client input from overwriting identifiers, audit fields, owner, privileged
   attributes, or other server-controlled data.
4. Add Flyway migrations matching JPA names, types, constraints, enum representation, foreign keys, relationships, and
   indexes. Prefer database constraints as a final integrity barrier where appropriate.
5. Implement only requested CRUD and list behavior. Allow-list sortable fields and directions rather than passing
   arbitrary client values into persistence sorting. Validate pagination and filters and return clear client errors for
   invalid input.
6. Apply ownership filters only to user-owned data. Add soft deletion only when requested or required by an explicit
   domain rule.

Do not expose mutable entities through API endpoints or accept client-calculated/server-authoritative values (for
example, prices, balances, totals, roles, or inventory) without an explicit requirement and validation policy.

## Security

When JWT is enabled:

- Configure authorization rules explicitly. Keep only intended authentication and documentation routes public; protect
  domain routes by default.
- Use strong password hashing. Validate token signature, issuer, expiry, and intended token type. Keep access and
  refresh tokens distinct; store refresh-token hashes when persisted and revoke/rotate them safely.
- Enforce role checks and ownership at the service or query boundary, not only by hiding UI actions or relying on route
  names. Do not allow users to access one another's records unless the domain says the data is shared.
- Keep JWT keys, database credentials, and bootstrap credentials outside source control. Validate required production
  configuration and document safe local development settings.
- Add CORS rules only when a client origin or deployment requirement is specified. For stateless APIs, configure CSRF
  and sessions deliberately rather than copying defaults blindly.

## Exceptions and transaction handling

- Define domain exceptions for missing resources, conflicts, invalid operations, and access violations where applicable.
  Translate them centrally to stable HTTP statuses and a consistent response body.
- Handle Bean Validation failures with field-level messages. Handle authentication and access-denied errors consistently
  with API errors.
- Do not catch and suppress failures in service operations. Keep transaction orchestration in service methods, and
  ensure failed writes roll back; configure rollback for checked exceptions if those are used.
- Avoid returning exception messages from infrastructure or unexpected failures. Log useful diagnostic context without
  secrets or sensitive payloads, and return a generic server error to clients.
- Map database constraint and concurrency failures to deliberate conflict responses where appropriate, without partially
  applying changes.

## Domain workflows

Implement workflows only when stated in the project definition. For any multi-step operation involving related records
or state changes:

- Identify authoritative data, invariants, allowed transitions, concurrency needs, and failure behavior before coding.
  Do not invent cancellation, refund, ownership, inventory, or lifecycle policies.
- Put orchestration and transaction demarcation in services. Validate all required conditions and persist related
  changes atomically when they share one database transaction.
- Use appropriate locking or optimistic versioning for concurrent updates, and handle stale writes or lock conflicts
  without partial effects.
- Keep audit/history records append-only in application behavior when the domain requires immutable history. Record
  relevant actor, time, prior/new values, and return results in a deliberate order.
- Do not claim atomicity across external systems. Use an outbox or another reliable delivery design if external side
  effects become part of a workflow.

## Configuration, docs, and verification

- Use `application.yaml` environment-variable overrides for credentials and token settings. Disable Hibernate DDL
  mutation and configure UTC handling deliberately.
- Document configuration, routes, authorization, domain behavior, assumptions, and error responses in the
  README/OpenAPI.
- Add focused tests for requested domain behavior, validation, security boundaries, mappings, and persistence/API
  integration. Keep tests isolated from external services where feasible. Cover rollback and concurrency when the
  defined workflow depends on them.
- Run the relevant Maven build or tests when permitted, fix failures caused by the changes, and report exactly what was
  run. Never claim verification passed if it was not run.
- Inspect the destination before editing. Preserve existing conventions and unrelated files. Search generated output for
  stale sample names and remove them.
- Do not add domain-specific extras such as scheduled jobs, notifications, status workflows, exports, or ownership
  features unless the project definition calls for them.
