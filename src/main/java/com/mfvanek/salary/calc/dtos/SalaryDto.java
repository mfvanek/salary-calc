package com.mfvanek.salary.calc.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
public class SalaryDto {

    @Id
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
