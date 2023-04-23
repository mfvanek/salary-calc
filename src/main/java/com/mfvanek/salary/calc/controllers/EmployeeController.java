package com.mfvanek.salary.calc.controllers;

import com.mfvanek.salary.calc.dtos.EmployeeDto;
import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.requests.EmployeeCreationRequest;
import com.mfvanek.salary.calc.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * I do mapping to DTO in controller.
 * https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
 * https://www.javadevjournal.com/spring/data-conversion-spring-rest-api/
 * https://auth0.com/blog/automatically-mapping-dto-to-entity-on-spring-boot-apis/
 */
@RestController
@RequestMapping(path = "/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable final UUID id) {
        final Optional<Employee> employee = employeeService.findById(id);
        final HttpStatus status = employee.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        final EmployeeDto employeeDto = convertToDto(employee.orElse(new Employee()));
        return new ResponseEntity<>(employeeDto, status);
    }

    @GetMapping("/all")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        final List<Employee> allEmployees = employeeService.getAll();
        final List<EmployeeDto> allEmployeesDto = allEmployees.stream()
                .map(this::convertToDto)
                .toList();
        return ResponseEntity.ok(allEmployeesDto);
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody final EmployeeCreationRequest newEmployee,
                                                      final UriComponentsBuilder uriComponentsBuilder) {
        final Employee employee = employeeService.create(newEmployee);
        final UriComponents uriComponents = uriComponentsBuilder.path("/employee/{id}").buildAndExpand(employee.getId());
        final HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        final EmployeeDto employeeDto = convertToDto(employee);
        return new ResponseEntity<>(employeeDto, headers, HttpStatus.CREATED);
    }

    private EmployeeDto convertToDto(final Employee employee) {
        return modelMapper.map(employee, EmployeeDto.class);
    }
}
