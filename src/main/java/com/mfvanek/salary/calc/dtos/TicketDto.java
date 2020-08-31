package com.mfvanek.salary.calc.dtos;

import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
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
