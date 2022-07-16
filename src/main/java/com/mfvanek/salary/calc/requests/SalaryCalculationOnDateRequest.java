package com.mfvanek.salary.calc.requests;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class SalaryCalculationOnDateRequest {

    @NotNull
    private UUID employeeId;

    @NotNull
    @Min(value = 0)
    private int workingDaysCount;

    @NotNull
    private LocalDate calculationDate;
}
