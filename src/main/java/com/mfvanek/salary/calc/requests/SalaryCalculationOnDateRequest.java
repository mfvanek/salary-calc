package com.mfvanek.salary.calc.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Data
@SuperBuilder
@RequiredArgsConstructor
public class SalaryCalculationOnDateRequest {

    @NotNull
    private UUID employeeId;

    @Min(0)
    private int workingDaysCount;

    @NotNull
    private LocalDate calculationDate;
}
