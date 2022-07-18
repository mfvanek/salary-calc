package com.mfvanek.salary.calc.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.entities.Ticket;
import com.mfvanek.salary.calc.repositories.TicketRepository;
import com.mfvanek.salary.calc.requests.SalaryCalculationOnDateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;

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

    @Transactional
    public Ticket create(final Employee employee, final SalaryCalculationOnDateRequest request) {
        // Let's check if it already exists
        Optional<Ticket> ticket = findExisting(request.getEmployeeId(), request.getCalculationDate());
        if (ticket.isPresent()) {
            return ticket.get();
        }

        // If it doesn't exist, let's try to add a new one
        try {
            final Ticket newTicket = Ticket.builder()
                    .id(UUID.randomUUID())
                    .calculationDate(request.getCalculationDate())
                    .employeeId(employee)
                    .isActive(true)
                    .calculationParamsJson(objectMapper.writeValueAsString(request))
                    .build();
            return ticketRepository.save(newTicket);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }

        ticket = findExisting(request.getEmployeeId(), request.getCalculationDate());
        if (ticket.isPresent()) {
            return ticket.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    private Optional<Ticket> findExisting(final UUID employeeId, final LocalDate calculationDate) {
        return ticketRepository.findByEmployeeIdAndCalculationDate(employeeId, calculationDate);
    }
}
