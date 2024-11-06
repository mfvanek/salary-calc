package com.mfvanek.salary.calc.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@RequiredArgsConstructor
public final class TicketDto {

    @NotNull
    private UUID id;

    @NotNull
    private ZonedDateTime calculationDate;

    @NotNull
    private UUID employeeId;

    private Boolean isActive;

    private SalaryDto salaryId;
}
