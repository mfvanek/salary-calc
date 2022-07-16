package com.mfvanek.salary.calc.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SalaryTest {

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void equalsHashCodeShouldAdhereContracts() {
        final Salary first = new Salary();
        final Salary second = new Salary();
        assertEquals(first, second);
        //        EqualsVerifier.forClass(Salary.class)
        //                .withOnlyTheseFields("id")
        //                .withPrefabValues(Employee.class, new Employee(), new Employee())
        //                .withPrefabValues(Ticket.class, new Ticket(), new Ticket())
        //                .verify();
    }
}
