package com.mfvanek.salary.calc.repositories;

import com.mfvanek.salary.calc.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    @Query(value = "select * from tickets t where t.emp_id = ?1 and t.calculated_at = ?2 and t.is_active = true",
        nativeQuery = true)
    Optional<Ticket> findByEmployeeIdAndCalculationDate(UUID employeeId, LocalDate calculationDate);
}
