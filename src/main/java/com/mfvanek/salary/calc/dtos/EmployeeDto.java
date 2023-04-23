package com.mfvanek.salary.calc.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Data
@Validated
@SuperBuilder
@RequiredArgsConstructor
public final class EmployeeDto {

    @NotNull
    private UUID id;

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
    @EqualsAndHashCode.Exclude
    private BigDecimal salaryPerHour;

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    @EqualsAndHashCode.Include
    private BigDecimal getSalaryForEquals() {
        // https://stackoverflow.com/questions/36625347/how-to-make-lomboks-equalsandhashcode-work-with-bigdecimal
        return Optional.ofNullable(salaryPerHour).map(BigDecimal::stripTrailingZeros).orElse(null);
    }
}
