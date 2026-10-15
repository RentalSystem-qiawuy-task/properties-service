# Local and Deployment Configuration

## Local development

The project root `.env` file is used by Docker Compose. Do not commit it. Copy `.env.example` to `.env` only when creating a local environment for the first time, then set a local database password.

Start PostgreSQL/PostGIS, Kafka, and the service:

```powershell
docker compose up -d --build
```

When running the application directly on Windows, use the host addresses published by Compose: PostgreSQL `localhost:5433` and Kafka `localhost:9092`. Spring Boot does not automatically load `.env` into a process launched by `gradlew`. In PowerShell, load the local file into the current process, then run the service:

```powershell
Get-Content .env | Where-Object { $_ -match '^\s*[A-Za-z_][A-Za-z0-9_]*\s*=' } | ForEach-Object {
    $propertyKey, $propertyValue = $_ -split '=', 2
    [Environment]::SetEnvironmentVariable($propertyKey.Trim(), $propertyValue.Trim(), 'Process')
}
.\gradlew.bat bootRun
```

When the service runs inside Compose, PostgreSQL is `postgres:5432` and Kafka is `kafka:29092`; `docker-compose.yaml` sets the Kafka override for the container.

## HTTP endpoints

- Swagger UI: <http://localhost:8083/swagger-ui/index.html>
- OpenAPI JSON: <http://localhost:8083/v3/api-docs>
- Actuator health: <http://localhost:8083/actuator/health>
- Kubernetes-style liveness/readiness: `/actuator/health/liveness` and `/actuator/health/readiness`

Swagger UI and OpenAPI JSON default to disabled in the base application configuration. Docker Compose enables them by default for local development; set `SWAGGER_UI_ENABLED=false` and `OPENAPI_ENABLED=false` in `.env` when running the Compose stack in a deployment. For a host-run local process, set both variables to `true` if the docs are needed. This service currently has no Spring Security integration; access control should be handled by the deployment/network boundary until authentication is implemented separately.

Actuator exposes only `health` and `info` by default. Health component details are hidden. Kafka's health contributor is disabled so a temporary broker outage does not by itself mark the HTTP service unhealthy. Database health remains part of the standard health result.

## Kafka setup

The project currently has no Kafka event DTOs, producers, or `@KafkaListener` consumers. No topic, producer call, or business event is created by this configuration.

The auto-configured producer uses string keys and values, acknowledgements from all in-sync replicas, retries, and idempotent delivery. The consumer uses string keys and values, the configured group, earliest offset reset by default, and disabled auto-commit. Listener containers acknowledge each record after successful processing. Most listener failures receive three retries with a one-second delay; Spring-classified fatal deserialization/conversion failures may go directly to recovery. If recovery fails, the error handler throws so the record is not silently acknowledged. Because there is no agreed event contract or DLT topic provisioned, a repeatedly failing record can block its partition until the issue is addressed. Define a compatible event contract and provision a DLT before changing that policy.

Useful local checks:

```powershell
docker compose ps
docker compose logs kafka --tail 100
docker compose exec kafka /opt/kafka/bin/kafka-broker-api-versions.sh --bootstrap-server kafka:29092
```

These checks verify broker readiness. End-to-end message delivery should be checked after the service's event contract and listener/producer are implemented.

## Environment variables

| Variable | Default/example | Purpose |
| --- | --- | --- |
| `SERVER_PORT` | `8083` | HTTP server port |
| `SHUTDOWN_TIMEOUT` | `20s` | Graceful shutdown wait |
| `DB_HOST`, `DB_PORT` | `localhost`, `5433` | PostgreSQL host and host-published port |
| `DB_NAME`, `SCHEMA_NAME` | `RoomRental`, `properties_service` | Database and schema |
| `DB_USER`, `DB_PASSWORD` | `postgres`, local placeholder | Database credentials; inject real credentials through deployment secrets |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` | Broker address for a process running on the host |
| `KAFKA_CONSUMER_GROUP_ID` | `properties-service` | Consumer group |
| `KAFKA_AUTO_OFFSET_RESET` | `earliest` | Start position when the group has no committed offset |
| `KAFKA_PRODUCER_RETRIES` | `10` | Producer retry count; idempotence is enabled |
| `OPENAPI_ENABLED`, `SWAGGER_UI_ENABLED` | `true` | Enable OpenAPI JSON and Swagger UI |
| `ACTUATOR_EXPOSED_ENDPOINTS` | `health,info` | Actuator web endpoint allowlist |
| `LOGGING_LEVEL_ROOT` | `INFO` | Root logging level |
| `EUREKA_CLIENT_ENABLED`, `EUREKA_SERVER_URL` | `false`, localhost URL | Optional service discovery |
| `SNOWFLAKE_GENERATOR_ID` | `0` | Unique Snowflake generator ID per service instance |

The Compose service receives `kafka:29092` for Kafka and `postgres:5432` for PostgreSQL. Do not pass host-side `localhost` addresses to containers: inside a container, `localhost` refers to that container itself. The current Compose Kafka listener uses plaintext for local development only; configure broker TLS/SASL and inject credentials through deployment secrets before using a secured production cluster.

## Build and run

```powershell
.\gradlew.bat test
.\gradlew.bat bootJar
docker compose up -d --build
```

Use the project's Java 21 toolchain. Flyway remains enabled, Hibernate DDL generation remains disabled, and schema changes continue to be managed by the existing migrations.
