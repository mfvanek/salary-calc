package com.mfvanek.salary.calc.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@RequiredArgsConstructor
public final class SalaryDto {

    @NotNull
    private UUID id;

    @NotNull
    private ZonedDateTime calculationDate;

    private int workingDaysCount;

    @NotNull
    private BigDecimal totalAmount;

    @NotNull
    private UUID employeeId;
}
