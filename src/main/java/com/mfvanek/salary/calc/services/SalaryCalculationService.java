package com.mfvanek.salary.calc.services;

import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.entities.Salary;
import com.mfvanek.salary.calc.entities.Ticket;
import com.mfvanek.salary.calc.repositories.SalaryRepository;
import com.mfvanek.salary.calc.repositories.TicketRepository;
import com.mfvanek.salary.calc.requests.SalaryCalculationOnDateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class SalaryCalculationService {

    private final SalaryRepository salaryRepository;
    private final TicketRepository ticketRepository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final CompletionService<Salary> completionService = new ExecutorCompletionService<>(executorService);

    public Future<Salary> submit(final Ticket ticket, final Employee employee, final SalaryCalculationOnDateRequest request) {
        return completionService.submit(() -> calculateOnDate(ticket, employee, request));
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(500, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            // TODO write to log
            executorService.shutdownNow();
        }
    }

    // TODO need transaction!
    private Salary calculateOnDate(final Ticket ticket, final Employee employee, final SalaryCalculationOnDateRequest request) {
        // TODO
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.warn(e.getMessage(), e);
        }

        final BigDecimal totalAmount = calculateTotalAmount(employee, request.getWorkingDaysCount());
        final Salary newSalary = Salary.builder()
                .id(UUID.randomUUID())
                .calculationDate(request.getCalculationDate())
                .workingDaysCount(request.getWorkingDaysCount())
                .totalAmount(totalAmount)
                .employeeId(employee)
                .ticket(ticket)
                .build();
        final Salary salary = salaryRepository.save(newSalary);
        markTicketAsFinished(ticket, salary);
        return salary;
    }

    private void markTicketAsFinished(final Ticket ticket, final Salary salary) {
        ticket.setIsActive(null);
        ticket.setSalaryId(salary);
        ticketRepository.save(ticket);
    }

    private BigDecimal calculateTotalAmount(final Employee employee, final int workingDaysCount) {
        final BigDecimal salaryPerHour = employee.getSalaryPerHour();
        final BigDecimal totalHours = BigDecimal.valueOf(workingDaysCount)
                .multiply(BigDecimal.valueOf(employee.getStandardHoursPerDay()));
        return salaryPerHour.multiply(totalHours);
    }
}
