package com.mfvanek.salary.calc.requests;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@RequiredArgsConstructor
public class SalaryCalculationOnDateRequest {

    @NotNull
    private UUID employeeId;

    @NotNull
    @Min(0)
    private int workingDaysCount;

    @NotNull
    private LocalDate calculationDate;
}
