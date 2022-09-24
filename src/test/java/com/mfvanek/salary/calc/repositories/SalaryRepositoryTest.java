package com.mfvanek.salary.calc.repositories;

import com.mfvanek.salary.calc.entities.Salary;
import com.mfvanek.salary.calc.support.TestBase;
import com.mfvanek.salary.calc.support.TestDataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SalaryRepositoryTest extends TestBase {

    @Autowired
    private SalaryRepository salaryRepository;

    @Test
    void correctness() {
        final var first = TestDataProvider.prepareIvanIvanov();
        final var second = TestDataProvider.preparePetrPetrov();
        employeeRepository.saveAll(List.of(first, second));

        final Salary firstSalary = TestDataProvider.prepareSalary()
                .calculationDate(LocalDate.now(clock))
                .employeeId(first)
                .build();
        final Salary secondSalary = TestDataProvider.prepareSalary()
                .calculationDate(LocalDate.now(clock))
                .employeeId(second)
                .build();
        assertThatEntityIsCorrect(Set.of(firstSalary, secondSalary), salaryRepository);

        assertInTransaction(() ->
                assertThat(salaryRepository.findAll())
                        .hasSize(2)
                        .allSatisfy(s ->
                                assertThat(s.getEmployeeId())
                                        .isNotNull()
                                        .satisfies(e -> assertThat(e.getSalaries())
                                                .hasSize(1)
                                                .first()
                                                .isEqualTo(s)))
        );
    }

    @Test
    void entitiesShouldNotNaveNullableColumnsOfPrimitiveTypes() {
        assertThatAllEntitiesDoesNotHavePrimitiveNullableFields("com.mfvanek.salary.calc", 3);
    }
}
