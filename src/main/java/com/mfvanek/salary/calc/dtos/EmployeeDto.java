package com.mfvanek.salary.calc.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.UUID;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Validated
@SuperBuilder
@RequiredArgsConstructor
public class EmployeeDto {

    @Id
    @NotNull
    private UUID id;

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
