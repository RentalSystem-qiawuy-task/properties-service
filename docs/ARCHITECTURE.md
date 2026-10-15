# Current Architecture

## Project profile

- **Service:** `properties-service`
- **Base Java package:** `fptu.semester8.mss301.propertiesservice`
- **Application entry point:** `PropertiesServiceApplication`
- **Framework and language:** Spring Boot 4.1.1, Java 21
- **Build tool:** Gradle Kotlin DSL
- **Persistence and schema changes:** Spring Data JPA, PostgreSQL, Flyway SQL migrations

## Current implementation

The Java source currently contains only the Spring Boot application entry point. There are no controllers, request/response DTOs, application services, domain models, JPA entities, repositories, mappers, or application-specific configuration classes yet. The project therefore has no implemented business feature layers to move or refactor.

The existing `PropertiesServiceApplicationTests` is an application-context smoke test in the base package. It remains there because it tests application-wide startup rather than one business feature.

Flyway migrations and their source data are kept under `src/main/resources/db/migration`. They are database resources, not Java feature packages. The administrative CSV importer is a project script under `scripts/` and is not part of Spring component scanning.

## Package organization

The first business capability represented by this service is administrative location data: provinces and wards (and the migration history for districts). The Java package tree reserves a feature boundary for it:

```text
fptu.semester8.mss301.propertiesservice
├── PropertiesServiceApplication.java
├── common
│   ├── id
│   └── validation
├── config
├── features
│   └── location
│       ├── api/dto
│       ├── application
│       ├── domain
│       ├── infrastructure/persistence
│       ├── mapper
│       └── repository
└── [feature implementation is added here as it is developed]
```

The currently empty package folders are preserved with `.gitkeep` files. They are extension points, not claims that those layers already contain working code.

## Responsibilities and dependency direction

- `features.location.api` will contain HTTP endpoints and API DTOs. Controllers should delegate work rather than own business workflows or persistence logic.
- `features.location.application` will contain use-case orchestration and transaction boundaries when location operations are implemented.
- `features.location.domain` will contain location business concepts and invariants that do not need Spring MVC or persistence implementation details.
- `features.location.repository` is reserved for the feature's repository contract or pragmatic Spring Data repository convention. Do not duplicate interfaces in both this package and infrastructure.
- `features.location.infrastructure.persistence` is reserved for persistence adapters or mappings if the feature adopts ports and adapters. Add it only when a concrete adapter exists.
- `features.location.api.dto` contains external request/response models; persistence entities should not be returned directly from APIs.
- `features.location.mapper` is reserved for actual entity/domain/DTO mapping code. No empty mapper class is introduced.
- `common` is for components genuinely shared by multiple features. `common.id` can host shared ID-generation integration once the selected external ID library is wired in; feature-specific ID rules should remain feature-local.
- `config` is for application-wide framework configuration. Feature-specific configuration should live with that feature.

The intended dependency direction is API → application → domain-facing abstractions, with infrastructure implementing persistence or external-system details. Since those layers do not exist yet, this is a convention for new code, not a description of current runtime dependencies.

## Database resources

Flyway is configured to load `classpath:db/migration`; keep versioned SQL migrations in that directory and preserve the increasing version order. CSV and manifest inputs under `db/migration/data` are supporting source data, not migrations. Do not edit an already-applied versioned migration to change a live schema; add a new version instead.

Hibernate schema generation is disabled (`ddl-auto: none`), so Flyway owns schema changes. Keep database schema changes separate from Java package refactoring.

## Testing

`PropertiesServiceApplicationTests` remains in the root test package because it verifies that the whole Spring application context loads. Add feature-specific tests under `src/test/java/fptu/semester8/mss301/propertiesservice/features/<feature>/` when controllers, use cases, domain rules, or adapters are implemented. There are no feature tests to move today.

## Current limitations

- No business API or persistence implementation exists in Java yet.
- No concrete ID-generation library integration exists yet; the `common.id` package is only a reserved package at this stage.
- The package tree is a starting structure for the location feature, not a strict hexagonal architecture implementation.
- No general-purpose `utils` package is reserved; utilities should be placed with the feature that owns them unless multiple features truly share them.
