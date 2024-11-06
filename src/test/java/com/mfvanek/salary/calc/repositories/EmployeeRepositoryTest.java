package com.mfvanek.salary.calc.repositories;

import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.support.TestBase;
import com.mfvanek.salary.calc.support.TestDataProvider;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeRepositoryTest extends TestBase {

    @Test
    void createdAtShouldBeSetAutomaticallyOnSave() {
        final Employee notSaved = TestDataProvider.prepareIvanIvanov();
        assertThat(notSaved.getCreatedAt())
            .isNull();
        assertThat(notSaved.getUpdatedAt())
            .isNull();
        final Employee saved = employeeRepository.save(notSaved);
        assertThat(saved)
            .isNotNull()
            .satisfies(e -> assertThat(e.getCreatedAt())
                .isEqualTo(ZonedDateTime.of(LocalDateTime.of(1999, Month.DECEMBER, 31, 23, 59, 59), clock.getZone()))
                .isBefore(ZonedDateTime.now(Clock.systemUTC())))
            .satisfies(e -> assertThat(e.getUpdatedAt()).isNull());
    }

    @Test
    void createdAtShouldNotBeReplacedWhenExplicitlySet() {
        final Employee notSaved = TestDataProvider.prepareIvanIvanov();
        notSaved.setCreatedAt(ZonedDateTime.now(clock).minusYears(1L));
        assertThat(notSaved.getUpdatedAt())
            .isNull();
        final Employee saved = employeeRepository.save(notSaved);
        assertThat(saved)
            .isNotNull()
            .satisfies(e -> assertThat(e.getCreatedAt())
                .isEqualTo(ZonedDateTime.of(LocalDateTime.of(1998, Month.DECEMBER, 31, 23, 59, 59), clock.getZone()))
                .isBefore(ZonedDateTime.now(clock)))
            .satisfies(e -> assertThat(e.getUpdatedAt()).isNull());
    }

    @Test
    void canBeSavedInFuture() {
        final LocalDateTime distantFuture = LocalDateTime.of(3000, Month.JANUARY, 1, 0, 0, 0);
        mutableClock.setInstant(distantFuture.toInstant(FIXED_ZONE));

        final Employee notSaved = TestDataProvider.prepareIvanIvanov();
        assertThat(notSaved.getCreatedAt())
            .isNull();
        assertThat(notSaved.getUpdatedAt())
            .isNull();
        final Employee saved = employeeRepository.save(notSaved);
        assertThat(saved)
            .isNotNull()
            .satisfies(e -> assertThat(e.getCreatedAt())
                .isEqualTo(ZonedDateTime.of(distantFuture, clock.getZone()))
                .isAfter(ZonedDateTime.now(Clock.systemUTC())))
            .satisfies(e -> assertThat(e.getUpdatedAt()).isNull());
    }

    @Test
    void updatedAtShouldBeSetAutomaticallyOnUpdate() {
        final Employee notSaved = TestDataProvider.prepareIvanIvanov();
        assertThat(notSaved.getUpdatedAt())
            .isNull();
        final Employee saved = employeeRepository.save(notSaved);
        saved.setSalaryPerHour(saved.getSalaryPerHour().multiply(BigDecimal.TEN));
        final Employee updated = employeeRepository.saveAndFlush(saved);
        assertThat(updated)
            .isNotNull()
            .satisfies(e -> assertThat(e.getUpdatedAt())
                .isEqualTo(ZonedDateTime.now(clock)));
    }

    @Test
    void correctness() {
        final Employee first = TestDataProvider.prepareIvanIvanov();
        final Employee second = TestDataProvider.preparePetrPetrov();
        assertThatEntityIsCorrect(Set.of(first, second), employeeRepository);
    }
}
