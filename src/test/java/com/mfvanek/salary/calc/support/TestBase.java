package com.mfvanek.salary.calc.support;

import com.mfvanek.salary.calc.config.ClockHolder;
import com.mfvanek.salary.calc.entities.BaseEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = PostgresInitializer.class)
@AutoConfigureMockMvc
public abstract class TestBase {

    protected static final LocalDateTime BEFORE_MILLENNIUM = LocalDateTime.of(1999, Month.DECEMBER, 31, 23, 59, 59);

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected final long countRecordsInTable(@Nonnull final String tableName) {
        final var queryResult = jdbcTemplate.queryForObject("select count(*) from " + tableName, Long.class);
        return Objects.requireNonNullElse(queryResult, 0L);
    }

    @Nonnull
    protected Set<String> getTables() {
        return Set.of("employees", "salary_calc", "tickets");
    }

    @BeforeAll
    static void setUpClock() {
        final Clock fixed = Clock.fixed(BEFORE_MILLENNIUM.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        ClockHolder.setClock(fixed);
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
        final var saved = repository.saveAll(entities);
        assertThat(saved)
                .hasSameSizeAs(entities);
        saved.forEach(e -> {
                    assertThat(e.getCreatedAt())
                            .isEqualTo(BEFORE_MILLENNIUM);
                    assertThat(e.getUpdatedAt())
                            .isEqualTo(BEFORE_MILLENNIUM);
                });

        final var result = repository.findAll();
        assertThat(result)
                .hasSameSizeAs(entities);
        result.forEach(c -> assertThatNoException()
                .as("Метод toString не должен генерировать ошибок")
                .isThrownBy(c::toString)); // toString
    }
}
