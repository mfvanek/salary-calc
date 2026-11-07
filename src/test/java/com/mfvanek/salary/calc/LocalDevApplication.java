package com.mfvanek.salary.calc;

import com.mfvanek.salary.calc.support.PostgresContainerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
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
        PostgreSQLContainer postgresContainer() {
            return PostgresContainerConfiguration.newContainer()
                .withDatabaseName("salary_calc_demo")
                .withUsername("salary_calc_demo_user")
                .withPassword("salary_calc_demo_user_pwd");
        }
    }
}
