package com.mfvanek.salary.calc.services;

import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.entities.Salary;
import com.mfvanek.salary.calc.entities.Ticket;
import com.mfvanek.salary.calc.repositories.SalaryRepository;
import com.mfvanek.salary.calc.requests.SalaryCalculationOnDateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;

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
    public Ticket calculateOnDate(final SalaryCalculationOnDateRequest request) {
        Objects.requireNonNull(request);
        final Optional<Employee> employee = employeeService.findById(request.getEmployeeId());
        if (employee.isEmpty()) {
            throw new EntityNotFoundException(String.format("Employee with id = %s not found", request.getEmployeeId()));
        }
        final Ticket ticket = ticketService.create(employee.get(), request);
        salaryCalculationService.submit(ticket, employee.get(), request);
        return ticket;
    }
}
