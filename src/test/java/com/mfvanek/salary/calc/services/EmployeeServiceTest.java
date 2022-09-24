package com.mfvanek.salary.calc.services;

import com.mfvanek.salary.calc.support.TestBase;
import com.mfvanek.salary.calc.support.TestDataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeServiceTest extends TestBase {

    @Autowired
    private EmployeeService employeeService;

    @Test
    void findByIdShouldWork() {
        assertThat(employeeService.findById(UUID.randomUUID()))
                .isNotPresent();

        final var employee = employeeRepository.saveAndFlush(TestDataProvider.prepareIvanIvanov());
        assertThat(employeeService.findById(employee.getId()))
                .isPresent()
                .get()
                .satisfies(e -> {
                    assertThat(e.getId())
                            .isEqualTo(employee.getId());
                    assertThat(e.getFirstName())
                            .isEqualTo("Ivan");
                });
    }
}
