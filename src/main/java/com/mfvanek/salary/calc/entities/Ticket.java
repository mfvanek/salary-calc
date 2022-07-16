package com.mfvanek.salary.calc.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
