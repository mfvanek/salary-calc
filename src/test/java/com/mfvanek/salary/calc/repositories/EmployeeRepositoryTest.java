package com.mfvanek.salary.calc.repositories;

import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.support.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

class EmployeeRepositoryTest extends TestBase {

    @Autowired
    private EmployeeRepository repository;

    @Test
    void correctness() {
        final var first = Employee.builder()
                .id(UUID.randomUUID())
                .firstName("Ivan")
                .lastName("Ivanov")
                .salaryPerHour(new BigDecimal("324.45"))
                .standardHoursPerDay(8)
                .build();
        final var second = Employee.builder()
                .id(UUID.randomUUID())
                .firstName("Petr")
                .lastName("Petrov")
                .salaryPerHour(new BigDecimal("1098.12"))
                .standardHoursPerDay(8)
                .build();

        assertThatEntityIsCorrect(Set.of(first, second), repository);
    }
}
