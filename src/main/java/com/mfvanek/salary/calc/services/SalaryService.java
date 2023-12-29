package com.mfvanek.salary.calc.services;

import com.mfvanek.salary.calc.entities.Salary;
import com.mfvanek.salary.calc.entities.Ticket;
import com.mfvanek.salary.calc.repositories.SalaryRepository;
import com.mfvanek.salary.calc.requests.SalaryCalculationOnDateRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SalaryService {

    private final SalaryRepository salaryRepository;
    private final EmployeeService employeeService;
    private final TicketService ticketService;
    private final SalaryCalculationService salaryCalculationService;

    public Optional<Salary> findById(final UUID id) {
        Objects.requireNonNull(id, "id cannot be null");
        return salaryRepository.findById(id);
    }

    @Transactional
    public Ticket calculateOnDate(@Nonnull final SalaryCalculationOnDateRequest request) {
        Objects.requireNonNull(request, "request cannot be null");
        return employeeService.findById(request.getEmployeeId())
                .map(e -> {
                    final Ticket ticket = ticketService.create(e, request);
                    salaryCalculationService.submit(ticket, e, request);
                    return ticket;
                })
                .orElseThrow(() -> {
                    final String message = String.format("Employee with id = %s not found", request.getEmployeeId());
                    return new EntityNotFoundException(message);
                });
    }
}
