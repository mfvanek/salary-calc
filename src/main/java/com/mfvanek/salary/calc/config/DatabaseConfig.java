package com.mfvanek.salary.calc.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

@Profile("local")
@EnableTransactionManagement(proxyTargetClass = true)
@Configuration(proxyBeanMethods = false)
public class DatabaseConfig {

    public static final String PG_IMAGE = "postgres:14.5-alpine";

    @SuppressWarnings({"resource", "java:S2095", "java:S1452"})
    @Bean(initMethod = "start", destroyMethod = "stop")
    public JdbcDatabaseContainer<?> jdbcDatabaseContainer() {
        return new PostgreSQLContainer<>(PG_IMAGE)
                .withDatabaseName("salary_calc_demo")
                .withUsername("salary_calc_demo_user")
                .withPassword("salary_calc_demo_user_pwd")
                .waitingFor(Wait.forListeningPort());
    }

    @Bean
    public DataSource dataSource(@Nonnull final JdbcDatabaseContainer<?> jdbcDatabaseContainer) {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcDatabaseContainer.getJdbcUrl());
        hikariConfig.setUsername(jdbcDatabaseContainer.getUsername());
        hikariConfig.setPassword(jdbcDatabaseContainer.getPassword());
        return new HikariDataSource(hikariConfig);
    }
}
