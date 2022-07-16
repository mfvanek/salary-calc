package com.mfvanek.salary.calc.requests;

import lombok.Data;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EmployeeCreationRequest {

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    @Min(value = 1)
    @Max(value = 8)
    private int standardHoursPerDay;

    @NotNull
    @DecimalMax(value = "5000.00")
    @DecimalMin(value = "100.00")
    private BigDecimal salaryPerHour;
}
