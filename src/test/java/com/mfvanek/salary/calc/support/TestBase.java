package com.mfvanek.salary.calc.support;

import com.mfvanek.salary.calc.entities.BaseEntity;
import com.mfvanek.salary.calc.repositories.EmployeeRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.support.TransactionTemplate;
import org.threeten.extra.MutableClock;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SuppressWarnings("PMD.ExcessiveImports")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = TestBase.CustomClockConfiguration.class, initializers = PostgresInitializer.class)
public abstract class TestBase {

    protected static final ZoneOffset FIXED_ZONE = ZoneOffset.ofHours(-1);
    private static final LocalDateTime BEFORE_MILLENNIUM = LocalDateTime.of(1999, Month.DECEMBER, 31, 23, 59, 59);

    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected MutableClock mutableClock;
    @Autowired
    protected Clock clock;
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

    @AfterEach
    void resetClock() {
        mutableClock.setInstant(BEFORE_MILLENNIUM.toInstant(FIXED_ZONE));
    }

    protected final long countRecordsInTable(@Nonnull final String tableName) {
        final Long queryResult = jdbcTemplate.queryForObject("select count(*) from " + tableName, Long.class);
        return Objects.requireNonNullElse(queryResult, 0L);
    }

    @Nonnull
    protected Set<String> getTables() {
        return Set.of("employees", "salary_calc", "tickets");
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected <T extends BaseEntity> void assertThatEntityIsCorrect(@Nonnull final Set<T> entities,
                                                                    @Nonnull final JpaRepository<T, UUID> repository) {
        assertThat(entities)
            .as("The size of the collection must be greater than or equal to two")
            .hasSizeGreaterThanOrEqualTo(2);
        entities.forEach(e -> assertThat(e.getId())
            .as("The ID must be filled in before saving")
            .isNotNull());
        assertThat(entities.stream().map(BaseEntity::getId).distinct().count())
            .as("All identifiers must be unique %s", entities)
            .isEqualTo(entities.size());
        final List<T> saved = repository.saveAll(entities);
        assertThat(saved)
            .hasSameSizeAs(entities);
        saved.forEach(e -> {
            final ZonedDateTime expected = beforeMillennium();
            assertThat(e.getCreatedAt())
                .isEqualTo(expected);
            if (e.getUpdatedAt() != null) {
                assertThat(e.getUpdatedAt())
                    .isEqualTo(expected);
            }
        });

        final List<T> result = repository.findAll();
        assertThat(result)
            .hasSameSizeAs(entities);
        result.forEach(c -> assertThatNoException()
            .as("Метод toString не должен генерировать ошибок")
            .isThrownBy(c::toString)); // toString
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

    @Nonnull
    protected ZonedDateTime beforeMillennium() {
        return ZonedDateTime.of(BEFORE_MILLENNIUM, clock.getZone());
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

    @TestConfiguration
    static class CustomClockConfiguration {

        @Bean
        public MutableClock mutableClock() {
            return MutableClock.of(BEFORE_MILLENNIUM.toInstant(FIXED_ZONE), FIXED_ZONE);
        }

        @Bean
        @Primary
        public Clock fixedClock(@Nonnull final MutableClock mutableClock) {
            return mutableClock;
        }
    }
}
