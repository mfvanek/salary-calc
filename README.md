# salary-calc
Demo app for salary calculation (Spring Boot)

[![Java CI](https://github.com/mfvanek/salary-calc/actions/workflows/tests.yml/badge.svg)](https://github.com/mfvanek/salary-calc/actions/workflows/tests.yml)

[![Total lines](https://tokei.rs/b1/github/mfvanek/salary-calc)](https://github.com/mfvanek/salary-calc)
[![Files](https://tokei.rs/b1/github/mfvanek/salary-calc?category=files)](https://github.com/mfvanek/salary-calc)

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=mfvanek_salary-calc&metric=bugs)](https://sonarcloud.io/summary/new_code?id=mfvanek_salary-calc)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=mfvanek_salary-calc&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=mfvanek_salary-calc)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=mfvanek_salary-calc&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=mfvanek_salary-calc)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=mfvanek_salary-calc&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=mfvanek_salary-calc)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=mfvanek_salary-calc&metric=coverage)](https://sonarcloud.io/summary/new_code?id=mfvanek_salary-calc)

## Requirements
Java 17+  
Spring Boot 3.0+

## API
[Swagger Doc](http://localhost:8090/v3/api-docs)  
[Swagger UI](http://localhost:8090/actuator/swagger-ui)

[Health](http://localhost:8090/actuator/health)

http://localhost:8080/api/employee/dcffa631-595a-44e6-8f8c-1c077de895bf

## Build image
```
mvn spring-boot:build-image
```
