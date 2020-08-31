package com.mfvanek.salary.calc.services.interfaces;

import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.requests.EmployeeCreationRequest;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeService {

    Optional<Employee> findById(final UUID id);

    Employee create(final EmployeeCreationRequest request);
}
