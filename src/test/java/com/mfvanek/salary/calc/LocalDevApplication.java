package com.mfvanek.salary.calc;

import com.mfvanek.salary.calc.support.PostgresInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Runs the application locally with a PostgreSQL database started by Testcontainers.
 * Lives in test sources so that Testcontainers never ends up in the production artifact or native image.
 * Start it with {@code ./mvnw spring-boot:test-run}.
 */
public final class LocalDevApplication {

    private LocalDevApplication() {
    }

    public static void main(final String[] args) {
        SpringApplication.from(Application::main)
            .with(LocalDatabaseConfiguration.class)
            .run(args);
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class LocalDatabaseConfiguration {

        @Bean
        @ServiceConnection
        @SuppressWarnings({"resource", "java:S2095", "java:S1452"})
        PostgreSQLContainer postgresContainer() {
            return new PostgreSQLContainer(PostgresInitializer.PG_IMAGE)
                .withDatabaseName("salary_calc_demo")
                .withUsername("salary_calc_demo_user")
                .withPassword("salary_calc_demo_user_pwd")
                .waitingFor(Wait.forListeningPort());
        }
    }
}
