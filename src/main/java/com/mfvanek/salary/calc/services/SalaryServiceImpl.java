package com.mfvanek.salary.calc.services;

import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.entities.Salary;
import com.mfvanek.salary.calc.entities.Ticket;
import com.mfvanek.salary.calc.repositories.SalaryRepository;
import com.mfvanek.salary.calc.requests.SalaryCalculationOnDateRequest;
import com.mfvanek.salary.calc.services.interfaces.EmployeeService;
import com.mfvanek.salary.calc.services.interfaces.SalaryCalculationService;
import com.mfvanek.salary.calc.services.interfaces.SalaryService;
import com.mfvanek.salary.calc.services.interfaces.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class SalaryServiceImpl implements SalaryService {

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private SalaryCalculationService salaryCalculationService;

    @Transactional(readOnly = true)
    @Override
    public Optional<Salary> findById(final UUID id) {
        Objects.requireNonNull(id);
        return salaryRepository.findById(id);
    }

    @Override
    public Ticket calculateOnDate(final SalaryCalculationOnDateRequest request) {
        Objects.requireNonNull(request);
        final Optional<Employee> employee = employeeService.findById(request.getEmployeeId());
        if (!employee.isPresent()) {
            throw new EntityNotFoundException(String.format("Employee with id = %s not found", request.getEmployeeId()));
        }
        final Ticket ticket = ticketService.create(employee.get(), request);
        salaryCalculationService.submit(ticket, employee.get(), request);
        return ticket;
    }
}
