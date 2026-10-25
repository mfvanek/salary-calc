package com.mfvanek.salary.calc.services;

import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.repositories.EmployeeRepository;
import com.mfvanek.salary.calc.requests.EmployeeCreationRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @NonNull
    public Optional<Employee> findById(@NonNull final UUID id) {
        Objects.requireNonNull(id, "id cannot be null");
        return employeeRepository.findById(id);
    }

    @NonNull
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @NonNull
    @Transactional
    public Employee create(@NonNull final EmployeeCreationRequest request) {
        final Employee employee = Employee.builder()
            .id(UUID.randomUUID())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .standardHoursPerDay(request.getStandardHoursPerDay())
            .salaryPerHour(request.getSalaryPerHour())
            .build();
        return employeeRepository.save(employee);
    }
}
