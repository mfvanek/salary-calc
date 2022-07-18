package com.mfvanek.salary.calc.services;

import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.repositories.EmployeeRepository;
import com.mfvanek.salary.calc.requests.EmployeeCreationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmployeeService {

    private final Clock clock;
    private final EmployeeRepository employeeRepository;

    @Nonnull
    public Optional<Employee> findById(@Nonnull final UUID id) {
        Objects.requireNonNull(id);
        return employeeRepository.findById(id);
    }

    @Nonnull
    @Transactional
    public Employee create(@Nonnull final EmployeeCreationRequest request) {
        final Employee employee = Employee.builder()
                .id(UUID.randomUUID())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .standardHoursPerDay(request.getStandardHoursPerDay())
                .salaryPerHour(request.getSalaryPerHour())
                .createdAt(LocalDateTime.now(clock))
                .build();
        return employeeRepository.save(employee);
    }
}
