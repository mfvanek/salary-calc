package com.mfvanek.salary.calc.controllers;

import com.mfvanek.salary.calc.dtos.TicketDto;
import com.mfvanek.salary.calc.entities.Ticket;
import com.mfvanek.salary.calc.services.interfaces.TicketService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicket(@PathVariable UUID id) {
        final Optional<Ticket> ticket = ticketService.findById(id);
        final HttpStatus status = ticket.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        final TicketDto ticketDto = convertToDto(ticket.orElse(new Ticket()));
        return new ResponseEntity<>(ticketDto, status);
    }

    private TicketDto convertToDto(final Ticket ticket) {
        return modelMapper.map(ticket, TicketDto.class);
    }
}
