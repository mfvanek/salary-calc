package com.mfvanek.salary.calc.requests;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@RequiredArgsConstructor
public class EmployeeCreationRequest {

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    @Min(1)
    @Max(8)
    private int standardHoursPerDay;

    @NotNull
    @DecimalMax("5000.00")
    @DecimalMin("100.00")
    private BigDecimal salaryPerHour;
}
