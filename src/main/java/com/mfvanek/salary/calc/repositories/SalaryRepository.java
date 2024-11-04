package com.mfvanek.salary.calc.repositories;

import com.mfvanek.salary.calc.entities.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, UUID> {

}
