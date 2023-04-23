package com.mfvanek.salary.calc.dtos;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class EmployeeDtoTest {

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void equalsHashCodeShouldAdhereContracts() {
        EqualsVerifier.forClass(EmployeeDto.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }
}
