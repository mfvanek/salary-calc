# salary-calc

Demo app for salary calculation (Spring Boot)

[![Java CI](https://github.com/mfvanek/salary-calc/actions/workflows/tests.yml/badge.svg)](https://github.com/mfvanek/salary-calc/actions/workflows/tests.yml)

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=mfvanek_salary-calc&metric=bugs)](https://sonarcloud.io/summary/new_code?id=mfvanek_salary-calc)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=mfvanek_salary-calc&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=mfvanek_salary-calc)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=mfvanek_salary-calc&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=mfvanek_salary-calc)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=mfvanek_salary-calc&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=mfvanek_salary-calc)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=mfvanek_salary-calc&metric=coverage)](https://sonarcloud.io/summary/new_code?id=mfvanek_salary-calc)

## Requirements

- Java 21+  
- Spring Boot 3.5+

## API

### Actuator

- [Swagger UI](http://localhost:8090/actuator/swagger-ui)
- [Health](http://localhost:8090/actuator/health)

### Endpoints
```shell
curl http://localhost:8080/api/employee/dcffa631-595a-44e6-8f8c-1c077de895bf
```

```shell
curl -i -X POST -d "{\"firstName\": \"John\",\"lastName\": \"Wick\",\"standardHoursPerDay\": 8,\"salaryPerHour\": 5000}" http://localhost:8080/api/employee -H "Content-Type: application/json"
```

```shell
curl http://localhost:8080/api/employee/all
```

## Run locally

### With database from Testcontainers

```
mvn spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.jvmArguments="-Duser.timezone=Europe/Moscow"
```

### With external database

```
mvn spring-boot:run -Dspring-boot.run.profiles=extern -Dspring-boot.run.jvmArguments="-Duser.timezone=Europe/Moscow"
```

## Run in Docker

### Build image

```
mvn clean spring-boot:build-image -DskipTests -DskipSpotbugs=true
```

### Build native image

**_Unfortunately, built application doesn't start in container_**
```
mvn clean spring-boot:build-image -DskipTests -DskipSpotbugs=true -Pnative
```

### Docker Compose

#### Start

```shell
docker-compose --project-name="salary-calc" up -d
```

#### Stop

```shell
docker-compose --project-name="salary-calc" down
```
