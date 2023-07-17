package com.mfvanek.salary.calc.support;

import com.mfvanek.salary.calc.entities.Employee;
import lombok.experimental.UtilityClass;

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
                .build();
    }

    @Nonnull
    public static Employee preparePetrPetrov() {
        return Employee.builder()
                .id(UUID.randomUUID())
                .firstName("Petr")
                .lastName("Petrov")
                .build();
    }
}
