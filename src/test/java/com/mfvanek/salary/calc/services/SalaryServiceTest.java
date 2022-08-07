package com.mfvanek.salary.calc.services;

import com.mfvanek.salary.calc.entities.Salary;
import com.mfvanek.salary.calc.repositories.SalaryRepository;
import com.mfvanek.salary.calc.support.TestBase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SalaryServiceTest extends TestBase {

    @Autowired
    private SalaryRepository salaryRepository;
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

    @Disabled
    @Test
    void findByIdShouldReturnEntityWhenFound() {
        final Salary notSaved = Salary.builder()
                .id(UUID.randomUUID())
                .calculationDate(LocalDate.now(clock))
                .workingDaysCount(4)
                .totalAmount(new BigDecimal("12534.00"))
                .build();
        final Salary saved = salaryRepository.save(notSaved);
        assertThat(salaryService.findById(saved.getId()))
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
