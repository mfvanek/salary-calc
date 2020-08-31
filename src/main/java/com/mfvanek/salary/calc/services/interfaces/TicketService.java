package com.mfvanek.salary.calc.services.interfaces;

import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.entities.Ticket;
import com.mfvanek.salary.calc.requests.SalaryCalculationOnDateRequest;

import java.util.Optional;
import java.util.UUID;

public interface TicketService {

    Optional<Ticket> findById(final UUID id);

    Ticket create(final Employee employee, final SalaryCalculationOnDateRequest request);
}
