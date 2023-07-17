package com.mfvanek.salary.calc.support;

import com.mfvanek.salary.calc.repositories.EmployeeRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("PMD.ExcessiveImports")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = PostgresInitializer.class)
public abstract class TestBase {

    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected EmployeeRepository employeeRepository;
    @Autowired
    protected WebTestClient webTestClient;
    @LocalServerPort
    protected int port;
    @LocalManagementPort
    protected int actuatorPort;
    @Autowired
    private TransactionTemplate transactionTemplate;

    protected final long countRecordsInTable(@Nonnull final String tableName) {
        final var queryResult = jdbcTemplate.queryForObject("select count(*) from " + tableName, Long.class);
        return Objects.requireNonNullElse(queryResult, 0L);
    }

    @Nonnull
    protected Set<String> getTables() {
        return Set.of("employees", "tickets");
    }

    @BeforeEach
    void check() {
        getTables().stream()
                .map(this::countRecordsInTable)
                .forEach(count -> assertThat(count).isZero());
    }

    @AfterEach
    void truncateTables() {
        jdbcTemplate.execute("truncate table " + String.join(", ", getTables()));
    }

    protected void assertInTransaction(@Nonnull final Runnable check) {
        transactionTemplate.execute(ts -> {
            check.run();
            return null;
        });
    }

    protected void assertThatAllEntitiesDoesNotHavePrimitiveNullableFields() {
        final Set<Class<?>> allEntities = new Reflections("com.mfvanek.salary.calc").getTypesAnnotatedWith(Entity.class);
        assertThat(allEntities).hasSize(3);
        allEntities.forEach(this::assertThatNullableFieldsAreNotPrimitive);
    }

    private <T> void assertThatNullableFieldsAreNotPrimitive(final Class<T> entityClass) {
        Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class) &&
                        field.getAnnotation(Column.class).nullable()
                )
                .forEach(field -> assertThat(field.getType().isPrimitive())
                        .withFailMessage(String.format("In %s field %s is primitive", entityClass.getName(), field.getName()))
                        .isFalse()
                );
    }
}
