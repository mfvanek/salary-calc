package com.mfvanek.salary.calc.services.interfaces;

import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.requests.EmployeeCreationRequest;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;

public interface EmployeeService {

    @Nonnull
    Optional<Employee> findById(@Nonnull UUID id);

    @Nonnull
    Employee create(@Nonnull EmployeeCreationRequest request);
}
