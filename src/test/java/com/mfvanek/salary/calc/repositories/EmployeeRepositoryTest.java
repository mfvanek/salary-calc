package com.mfvanek.salary.calc.repositories;

import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.support.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeRepositoryTest extends TestBase {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void createdAtShouldBeSetAutomaticallyOnSave() {
        final Employee notSaved = prepareIvanIvanov();
        assertThat(notSaved.getCreatedAt())
                .isNull();
        final Employee saved = employeeRepository.save(notSaved);
        assertThat(saved)
                .isNotNull()
                .satisfies(e -> assertThat(e.getCreatedAt())
                        .isEqualTo(LocalDateTime.of(1999, Month.DECEMBER, 31, 23, 59, 59))
                        .isBefore(LocalDateTime.now(Clock.systemDefaultZone())));
    }

    @Test
    void canBeSavedInFuture() {
        final LocalDateTime distantFuture = LocalDateTime.of(3000, Month.JANUARY, 1, 0, 0, 0);
        mutableClock.setInstant(distantFuture.toInstant(ZoneOffset.UTC));

        final Employee notSaved = prepareIvanIvanov();
        assertThat(notSaved.getCreatedAt())
                .isNull();
        final Employee saved = employeeRepository.save(notSaved);
        assertThat(saved)
                .isNotNull()
                .satisfies(e -> assertThat(e.getCreatedAt())
                        .isEqualTo(LocalDateTime.of(3000, Month.JANUARY, 1, 0, 0, 0))
                        .isAfter(LocalDateTime.now(Clock.systemDefaultZone())));
    }

    @Test
    void correctness() {
        final var first = prepareIvanIvanov();
        final var second = Employee.builder()
                .id(UUID.randomUUID())
                .firstName("Petr")
                .lastName("Petrov")
                .salaryPerHour(new BigDecimal("1098.12"))
                .standardHoursPerDay(8)
                .build();

        assertThatEntityIsCorrect(Set.of(first, second), employeeRepository);
    }

    @Nonnull
    private Employee prepareIvanIvanov() {
        return Employee.builder()
                .id(UUID.randomUUID())
                .firstName("Ivan")
                .lastName("Ivanov")
                .salaryPerHour(new BigDecimal("324.45"))
                .standardHoursPerDay(8)
                .build();
    }
}
