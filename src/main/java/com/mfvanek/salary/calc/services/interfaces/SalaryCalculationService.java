package com.mfvanek.salary.calc.services.interfaces;

import com.mfvanek.salary.calc.entities.Employee;
import com.mfvanek.salary.calc.entities.Salary;
import com.mfvanek.salary.calc.entities.Ticket;
import com.mfvanek.salary.calc.requests.SalaryCalculationOnDateRequest;

import java.util.concurrent.Future;

public interface SalaryCalculationService {

    Future<Salary> submit(final Ticket ticket, final Employee employee, final SalaryCalculationOnDateRequest request);
}
