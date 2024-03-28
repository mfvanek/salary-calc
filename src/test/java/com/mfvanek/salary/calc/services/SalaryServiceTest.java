package com.mfvanek.salary.calc.services;

import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.entities.Salary;
import com.mfvanek.salary.calc.entities.Ticket;
import com.mfvanek.salary.calc.requests.SalaryCalculationOnDateRequest;
import com.mfvanek.salary.calc.support.TestBase;
import com.mfvanek.salary.calc.support.TestDataProvider;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.UUID;
import javax.annotation.Nonnull;

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
        final Pair<UUID, UUID> ids = createEmployeeWithSalary();
        assertThat(salaryService.findById(ids.getRight()))
                .isPresent()
                .get()
                .satisfies(r -> {
                    assertThat(r.getId())
                            .isEqualTo(ids.getRight());
                    assertThat(r.getCreatedAt())
                            .isEqualTo(BEFORE_MILLENNIUM);
                    assertThat(r.getUpdatedAt())
                            .isNull();
                });
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void calculateOnDateShouldThrowExceptionOnInvalidRequest() {
        assertThatThrownBy(() -> salaryService.calculateOnDate(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("request cannot be null");

        final SalaryCalculationOnDateRequest request = SalaryCalculationOnDateRequest.builder().build();
        assertThatThrownBy(() -> salaryService.calculateOnDate(request))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("id cannot be null");
    }

    @Test
    void calculateOnDateShouldThrowExceptionWhenEmployeeDoesNotExist() {
        final SalaryCalculationOnDateRequest request = SalaryCalculationOnDateRequest.builder()
                .employeeId(UUID.fromString("2071becc-7866-46f3-938b-547b338bec80"))
                .build();
        assertThatThrownBy(() -> salaryService.calculateOnDate(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Employee with id = 2071becc-7866-46f3-938b-547b338bec80 not found");
    }

    @Test
    void calculateOnDateShouldWork() {
        final Pair<UUID, UUID> ids = createEmployeeWithSalary();

        final Ticket result = salaryService.calculateOnDate(SalaryCalculationOnDateRequest.builder()
                .employeeId(ids.getLeft())
                .workingDaysCount(0)
                .calculationDate(LocalDate.now(clock))
                .build());

        assertThat(result)
                .isNotNull();
    }

    @Nonnull
    private Pair<UUID, UUID> createEmployeeWithSalary() {
        final Salary notSaved = TestDataProvider.prepareSalary()
                .calculationDate(LocalDate.now(clock))
                .build();
        final Employee employee = TestDataProvider.prepareIvanIvanov()
                .withSalary(notSaved);
        employeeRepository.save(employee);
        return ImmutablePair.of(employee.getId(), notSaved.getId());
    }
}
