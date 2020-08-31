package com.mfvanek.salary.calc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.entities.Ticket;
import com.mfvanek.salary.calc.repositories.TicketRepository;
import com.mfvanek.salary.calc.requests.SalaryCalculationOnDateRequest;
import com.mfvanek.salary.calc.services.interfaces.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Ticket> findById(final UUID id) {
        Objects.requireNonNull(id);
        return ticketRepository.findById(id);
    }

    @Override
    public Ticket create(final Employee employee, final SalaryCalculationOnDateRequest request) {
        // Let's check if it already exists
        Optional<Ticket> ticket = findExisting(request.getEmployeeId(), request.getCalculationDate());
        if (ticket.isPresent()) {
            return ticket.get();
        }

        // If it doesn't exist, let's try to add a new one
        try {
            final Ticket newTicket = new Ticket(UUID.randomUUID(), request.getCalculationDate(),
                    employee, true, objectMapper.writeValueAsString(request), null);
            return ticketRepository.save(newTicket);
        } catch (Exception e) {
            // TODO write to log
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
