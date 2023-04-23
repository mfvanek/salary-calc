package com.mfvanek.salary.calc.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@SuperBuilder
@RequiredArgsConstructor
public final class SalaryDto {

    @NotNull
    private UUID id;

    @NotNull
    private LocalDate calculationDate;

    @NotNull
    private int workingDaysCount;

    @NotNull
    private BigDecimal totalAmount;

    @NotNull
    private UUID employeeId;
}
