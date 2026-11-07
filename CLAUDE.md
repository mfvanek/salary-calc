# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Demo Spring Boot 4 app (Java 25, Maven) for salary calculation backed by PostgreSQL 18. Single module, base package `com.mfvanek.salary.calc`.

## Commands

Use the Maven wrapper (`./mvnw`, or `mvnw.cmd` on Windows). Docker must be running: almost every test starts a PostgreSQL container via Testcontainers.

- Full build, same as CI: `./mvnw clean verify`
- Run a single test class / method: `./mvnw test -Dtest=SalaryServiceTest` / `./mvnw test -Dtest=SalaryServiceTest#methodName`
  - Add `-DskipSpotbugs=true -Dpmd.skip=true` for faster iteration (both are bound to the `test` phase and run after the tests).
- Run locally with a Testcontainers DB: `./mvnw spring-boot:test-run -Dspring-boot.run.jvmArguments="-Duser.timezone=Europe/Moscow"` (entry point `LocalDevApplication` in test sources)
- Run against an external DB on localhost:5432: `./mvnw spring-boot:run -Dspring-boot.run.profiles=extern`
- Docker image: `./mvnw clean spring-boot:build-image -DskipTests -DskipSpotbugs=true` (add `-Pnative` for a GraalVM native image)
- Full stack: `docker-compose --project-name="salary-calc" up -d` (the app uses the `docker` profile, host `postgres`)

## Quality gates (all fail the build)

- **Compiler**: `failOnWarning=true`, with Error Prone + NullAway (`OnlyNullMarked`) + errorprone-slf4j. Any new javac warning breaks the build.
- **Checkstyle** (`config/checkstyle/checkstyle.xml`) runs at the `compile` phase on main and test sources.
- **PMD** (`config/pmd/pmd.xml`) and **SpotBugs** (max effort, low threshold, with fb-contrib/findsecbugs; exclusions in `config/spotbugs/exclude.xml`) run at the `test` phase, tests included.
- **JaCoCo** coverage check at `prepare-package` (instruction ≥ 68%, branch ≥ 70%, small caps on missed classes/methods/lines).
- **DatabaseStructureStaticAnalysisTest** runs pg-index-health checks on the generated schema and asserts exact counts per diagnostic, and also asserts the PostgreSQL version string. Changing entities (columns, varchar lengths, comments) or the PG image usually means updating this test.

Code style: `final` on parameters and locals, Lombok for boilerplate (`lombok.config` sets `stopBubbling` and copies `@Value`/`@Qualifier` to constructors), jspecify `@NonNull`/`@Nullable`. Suppressions are written inline (`@SuppressWarnings("PMD...")`, `"java:S..."` for Sonar).

## Architecture

The usual layers: `controllers` → `services` → `repositories` (Spring Data JPA) → `entities`. Controllers accept `requests/*` objects and map entities to `dtos/*` with a shared `ModelMapper` (STRICT matching, private field access; see `AppConfig`). The servlet context path is `/api` on port 8080; actuator, Prometheus and Swagger UI are on management port 8090.

- **Schema**: there are no migrations. Hibernate generates the schema (`ddl-auto: create`) from the entities, and timestamps are stored in UTC.
- **Entities** extend `BaseEntity` (UUID id assigned by the caller, `createdAt`/`updatedAt` as `timestamptz`). Timestamps are set by `ClockAwareEntityListener`, a Spring `@Component` that gets the `Clock` bean through constructor injection. Field injection there does not work in the native image. Always get time from the injected `Clock`, never `now()` with no argument.
- **Async salary calculation**: `SalaryService.calculateOnDate` creates a `Ticket`, then `SalaryCalculationService` computes the `Salary` on its own executor (it includes an artificial 5s sleep), saves it, and marks the ticket finished. The API returns the ticket immediately with a `Location: /ticket/{id}` header.
- **Native image**: `HibernateRuntimeHints` (imported on `Application`) registers reflection hints that Hibernate needs but does not register itself. Add to it when a native build fails at runtime on reflection.

## Testing infrastructure

- Integration tests extend `support/TestBase`: a full `@SpringBootTest` on a random port with profile `test`, `WebTestClient`, and `JdbcTemplate`.
- PostgreSQL comes from `PostgresContainerConfiguration` through `@ServiceConnection`. The container lifecycle is tied to the Spring context, so the context cache shares one container across tests. `LocalDevApplication` reuses `newContainer()`. Keep the image tag (`PG_IMAGE`) in sync with `docker-compose.yml`.
- `TestBase` replaces `Clock` with a `MutableClock` fixed at 1999-12-31T23:59:59-01:00 and resets it after each test. Assertions on timestamps rely on this (`beforeMillennium()`).
- Before each test, `TestBase` asserts that the tables in `getTables()` (`employees`, `salary_calc`, `tickets`) are empty, and it truncates them after each test. Add new tables to `getTables()`. `TestBase` also asserts there are exactly 3 `@Entity` classes, so update that count too.
- Test fixtures are in `support/TestDataProvider`.
