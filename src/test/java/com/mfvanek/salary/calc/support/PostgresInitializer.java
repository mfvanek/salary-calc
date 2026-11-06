package com.mfvanek.salary.calc.support;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgresInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final String PG_IMAGE = "postgres:18.1";

    private static final DockerImageName IMAGE = DockerImageName.parse(PG_IMAGE);
    private static final Network NETWORK = Network.newNetwork();
    private static final PostgreSQLContainer CONTAINER = new PostgreSQLContainer(IMAGE);

    @Override
    public void initialize(final ConfigurableApplicationContext context) {
        CONTAINER
            .withNetwork(NETWORK)
            .withUrlParam("prepareThreshold", "0")
            .waitingFor(Wait.forListeningPort())
            .start();

        TestPropertyValues.of(
            "spring.datasource.url=" + CONTAINER.getJdbcUrl(),
            "spring.datasource.username=" + CONTAINER.getUsername(),
            "spring.datasource.password=" + CONTAINER.getPassword()
        ).applyTo(context.getEnvironment());
    }
}
