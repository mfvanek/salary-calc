package com.mfvanek.salary.calc.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

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
