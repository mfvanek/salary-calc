package com.mfvanek.salary.calc.requests;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

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
