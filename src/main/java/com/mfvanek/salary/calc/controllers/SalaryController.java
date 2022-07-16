package com.mfvanek.salary.calc.controllers;

import com.mfvanek.salary.calc.dtos.SalaryDto;
import com.mfvanek.salary.calc.dtos.TicketDto;
import com.mfvanek.salary.calc.entities.Salary;
import com.mfvanek.salary.calc.entities.Ticket;
import com.mfvanek.salary.calc.requests.SalaryCalculationOnDateRequest;
import com.mfvanek.salary.calc.services.interfaces.SalaryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/salary")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<SalaryDto> getSalaryCalculation(@PathVariable UUID id) {
        final Optional<Salary> salary = salaryService.findById(id);
        final HttpStatus status = salary.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        final SalaryDto salaryDto = convertToDto(salary.orElse(new Salary()));
        return new ResponseEntity<>(salaryDto, status);
    }

    @PostMapping("/onDate")
    public ResponseEntity<TicketDto> calculateSalary(@RequestBody SalaryCalculationOnDateRequest request,
                                                     UriComponentsBuilder uriComponentsBuilder) {
        final Ticket ticket = salaryService.calculateOnDate(request);
        final UriComponents uriComponents = uriComponentsBuilder.path("/ticket/{id}").buildAndExpand(ticket.getId());
        final HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        final TicketDto ticketDto = convertToDto(ticket);
        return new ResponseEntity<>(ticketDto, headers, HttpStatus.CREATED);
    }

    private SalaryDto convertToDto(final Salary salary) {
        return modelMapper.map(salary, SalaryDto.class);
    }

    private TicketDto convertToDto(final Ticket ticket) {
        return modelMapper.map(ticket, TicketDto.class);
    }
}
