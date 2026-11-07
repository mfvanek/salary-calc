package com.mfvanek.salary.calc.support;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Provides a PostgreSQL container for tests.
 * The datasource is configured automatically via {@link ServiceConnection},
 * and the container lifecycle is managed by the Spring application context.
 */
@TestConfiguration(proxyBeanMethods = false)
public class PostgresContainerConfiguration {

    public static final String PG_IMAGE = "postgres:18.1";

    /**
     * Creates a new (not started) PostgreSQL container with common settings.
     *
     * @return new container instance
     */
    @SuppressWarnings({"resource", "java:S2095", "java:S1452"})
    public static PostgreSQLContainer newContainer() {
        return new PostgreSQLContainer(DockerImageName.parse(PG_IMAGE))
            .waitingFor(Wait.forListeningPort());
    }

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgresContainer() {
        return newContainer()
            .withUrlParam("prepareThreshold", "0");
    }
}
