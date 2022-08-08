package com.mfvanek.salary.calc.services;

import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.entities.Salary;
import com.mfvanek.salary.calc.support.TestBase;
import com.mfvanek.salary.calc.support.TestDataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SalaryServiceTest extends TestBase {

    @Autowired
    private SalaryService salaryService;

    @Test
    void findByIdShouldThrowErrorOnNullArgument() {
        assertThatThrownBy(() -> salaryService.findById(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("id cannot be null");
    }

    @Test
    void findByIdShouldReturnEmptyOptionalWhenNotFound() {
        assertThat(salaryService.findById(UUID.randomUUID()))
                .isNotPresent();
    }

    @Test
    void findByIdShouldReturnEntityWhenFound() {
        final Employee employee = TestDataProvider.prepareIvanIvanov();
        final Salary notSaved = TestDataProvider.prepareSalary()
                .calculationDate(LocalDate.now(clock))
                .employeeId(employee)
                .build();
        employee.withSalary(notSaved);
        employeeRepository.save(employee);
        assertThat(salaryService.findById(notSaved.getId()))
                .isPresent()
                .get()
                .satisfies(r -> {
                    assertThat(r.getId())
                            .isEqualTo(notSaved.getId());
                    assertThat(r.getCreatedAt())
                            .isEqualTo(BEFORE_MILLENNIUM);
                    assertThat(r.getUpdatedAt())
                            .isNull();
                });
    }
}
