package com.mfvanek.salary.calc.support;

import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.entities.Salary;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.UUID;
import javax.annotation.Nonnull;

@SuppressWarnings("PMD.TestClassWithoutTestCases")
@UtilityClass
public class TestDataProvider {

    @Nonnull
    public static Employee prepareIvanIvanov() {
        return Employee.builder()
            .id(UUID.randomUUID())
            .firstName("Ivan")
            .lastName("Ivanov")
            .salaryPerHour(new BigDecimal("324.45"))
            .standardHoursPerDay(8)
            .build();
    }

    @Nonnull
    public static Employee preparePetrPetrov() {
        return Employee.builder()
            .id(UUID.randomUUID())
            .firstName("Petr")
            .lastName("Petrov")
            .salaryPerHour(new BigDecimal("1098.12"))
            .standardHoursPerDay(8)
            .build();
    }

    @Nonnull
    public Salary.SalaryBuilder<?, ?> prepareSalary() {
        return Salary.builder()
            .id(UUID.randomUUID())
            .workingDaysCount(4)
            .totalAmount(new BigDecimal("12534.00"));
    }
}
