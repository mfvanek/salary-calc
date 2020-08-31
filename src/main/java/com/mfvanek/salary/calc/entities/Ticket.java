package com.mfvanek.salary.calc.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "tickets",
        indexes = {
                @Index(name = "IDX_EMPLOYEE_DATE", columnList = "emp_id, calc_date, is_active", unique = true)
        }
)
public class Ticket {

    @Id
    @NotNull
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "calc_date", updatable = false, nullable = false)
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

    @OneToOne()
    @JoinColumn(name = "salary_id")
    private Salary salaryId;
}
