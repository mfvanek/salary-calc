package com.mfvanek.salary.calc.dtos;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class TicketDto {

    @Id
    @NotNull
    private UUID id;

    @NotNull
    private LocalDate calculationDate;

    @NotNull
    private UUID employeeId;

    private Boolean isActive;

    private SalaryDto salaryId;
}
