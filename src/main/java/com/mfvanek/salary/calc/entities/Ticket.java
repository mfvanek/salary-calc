package com.mfvanek.salary.calc.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
@Table(
        name = "tickets",
        indexes = {
                @Index(name = "idx_tickets_employee_date", columnList = "emp_id,calculated_at,is_active", unique = true)
        }
)
@Comment("Table for tickets")
public class Ticket extends BaseEntity {

    @NotNull
    @Column(name = "calculated_at", updatable = false, nullable = false)
    private LocalDate calculationDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", updatable = false, nullable = false)
    private Employee employeeId;

    @Column(name = "is_active")
    private Boolean isActive;

    @NotNull
    @Size(max = 2000)
    @Column(name = "calc_params", length = 2000, nullable = false)
    private String calculationParamsJson;

    @OneToOne
    @JoinColumn(name = "salary_id", unique = true)
    private Salary salaryId;
}
