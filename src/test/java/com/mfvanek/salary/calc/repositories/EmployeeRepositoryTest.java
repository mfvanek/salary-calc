package com.mfvanek.salary.calc.repositories;

import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.support.TestBase;
import com.mfvanek.salary.calc.support.TestDataProvider;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeRepositoryTest extends TestBase {

    @Test
    void createdAtShouldBeSetAutomaticallyOnSave() {
        final Employee notSaved = TestDataProvider.prepareIvanIvanov();
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

        final Employee notSaved = TestDataProvider.prepareIvanIvanov();
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
        final var first = TestDataProvider.prepareIvanIvanov();
        final var second = TestDataProvider.preparePetrPetrov();
        assertThatEntityIsCorrect(Set.of(first, second), employeeRepository);
    }
}
