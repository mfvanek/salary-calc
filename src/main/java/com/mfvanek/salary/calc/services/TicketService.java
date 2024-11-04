package com.mfvanek.salary.calc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.entities.Ticket;
import com.mfvanek.salary.calc.repositories.TicketRepository;
import com.mfvanek.salary.calc.requests.SalaryCalculationOnDateRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ObjectMapper objectMapper;

    public Optional<Ticket> findById(final UUID id) {
        Objects.requireNonNull(id);
        return ticketRepository.findById(id);
    }

    @SneakyThrows
    @Transactional
    public Ticket create(final Employee employee, final SalaryCalculationOnDateRequest request) {
        // Let's check if it already exists
        final Optional<Ticket> ticket = findExisting(request.getEmployeeId(), request.getCalculationDate());
        if (ticket.isPresent()) {
            return ticket.get();
        }

        // If it doesn't exist, let's try to add a new one
        final Ticket newTicket = Ticket.builder()
            .id(UUID.randomUUID())
            .calculationDate(request.getCalculationDate())
            .employeeId(employee)
            .isActive(Boolean.TRUE)
            .calculationParamsJson(objectMapper.writeValueAsString(request))
            .build();
        return ticketRepository.save(newTicket);
    }

    private Optional<Ticket> findExisting(final UUID employeeId, final LocalDate calculationDate) {
        return ticketRepository.findByEmployeeIdAndCalculationDate(employeeId, calculationDate);
    }
}
