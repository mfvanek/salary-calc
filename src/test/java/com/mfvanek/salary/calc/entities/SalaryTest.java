package com.mfvanek.salary.calc.entities;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SalaryTest {

    @Test
    @SuppressWarnings({"ConstantConditions", "EqualsBetweenInconvertibleTypes", "EqualsIncompatibleType", "PMD.EqualsNull"})
    void equalsHashCodeShouldAdhereContracts() {
        final Salary first = Salary.builder().build();
        final Salary second = new Salary(); // different creation way
        assertThat(first)
            .isEqualTo(first) // self
            .isEqualTo(second)
            .hasSameHashCodeAs(first) // self
            .hasSameHashCodeAs(second);

        // with null
        assertThat(first.equals(null))
            .isFalse();

        // with different type
        assertThat(first.equals(Integer.MAX_VALUE))
            .isFalse();

        final Salary third = Salary.builder()
            .id(UUID.randomUUID())
            .build();
        assertThat(third)
            .isNotEqualTo(first)
            .doesNotHaveSameHashCodeAs(first)
            .isNotEqualTo(second)
            .doesNotHaveSameHashCodeAs(second);

        first.setId(third.getId());
        assertThat(first)
            .isNotEqualTo(second)
            .doesNotHaveSameHashCodeAs(second)
            .isEqualTo(third)
            .hasSameHashCodeAs(third);
    }
}
