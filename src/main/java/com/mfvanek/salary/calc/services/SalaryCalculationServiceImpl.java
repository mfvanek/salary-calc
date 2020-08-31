package com.mfvanek.salary.calc.services;

import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.entities.Salary;
import com.mfvanek.salary.calc.entities.Ticket;
import com.mfvanek.salary.calc.repositories.SalaryRepository;
import com.mfvanek.salary.calc.repositories.TicketRepository;
import com.mfvanek.salary.calc.requests.SalaryCalculationOnDateRequest;
import com.mfvanek.salary.calc.services.interfaces.SalaryCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.*;

@Service
@Transactional
public class SalaryCalculationServiceImpl implements SalaryCalculationService {

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private TicketRepository ticketRepository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final CompletionService<Salary> completionService = new ExecutorCompletionService<>(executorService);

    @Override
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
            e.printStackTrace();
        }

        final BigDecimal totalAmount = calculateTotalAmount(employee, request.getWorkingDaysCount());
        final Salary newSalary = new Salary(UUID.randomUUID(),
                request.getCalculationDate(), request.getWorkingDaysCount(), totalAmount, employee, ticket);
        final Salary salary = salaryRepository.save(newSalary);
        markTicketAsFinished(ticket, salary);
        return salary;
    }

    private void markTicketAsFinished(final Ticket ticket, final Salary salary) {
        ticket.setIsActive(null);
        ticket.setSalaryId(salary);
        ticketRepository.save(ticket);
    }

    private BigDecimal calculateTotalAmount(final Employee employee, int workingDaysCount) {
        final BigDecimal salaryPerHour = employee.getSalaryPerHour();
        final BigDecimal totalHours = BigDecimal.valueOf(workingDaysCount * employee.getStandardHoursPerDay());
        return salaryPerHour.multiply(totalHours);
    }
}
