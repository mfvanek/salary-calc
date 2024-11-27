package com.mfvanek.salary.calc.services;

import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.entities.Ticket;
import com.mfvanek.salary.calc.requests.SalaryCalculationOnDateRequest;
import com.mfvanek.salary.calc.support.TestBase;
import com.mfvanek.salary.calc.support.TestDataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TicketServiceTest extends TestBase {

    @Autowired
    private TicketService ticketService;

    @Test
    void findByIdShouldWork() {
        assertThat(ticketService.findById(UUID.randomUUID()))
            .isNotPresent();
    }

    @Test
    void createShouldWork() {
        final Employee employee = employeeRepository.saveAndFlush(TestDataProvider.prepareIvanIvanov());
        final SalaryCalculationOnDateRequest request = SalaryCalculationOnDateRequest.builder()
            .calculationDate(ZonedDateTime.now(clock))
            .workingDaysCount(10)
            .employeeId(employee.getId())
            .build();
        final Ticket firstTicket = ticketService.create(employee, request);

        assertInTransaction(() ->
            assertThat(firstTicket)
                .isNotNull()
                .satisfies(t -> {
                    assertThat(t.getId())
                        .isNotNull();
                    assertThat(t.getEmployeeId().getId())
                        .isEqualTo(employee.getId());
                })
        );
        assertThat(countRecordsInTable("tickets")).isEqualTo(1);

        // Second creation shouldn't work
        final Ticket secondTicket = ticketService.create(employee, request);
        assertThat(secondTicket.getId()).isEqualTo(firstTicket.getId());
        assertThat(countRecordsInTable("tickets")).isEqualTo(1);

        assertThat(ticketService.findById(firstTicket.getId()))
            .isPresent()
            .get()
            .satisfies(t -> assertThat(t.getId()).isEqualTo(firstTicket.getId()));
    }
}
