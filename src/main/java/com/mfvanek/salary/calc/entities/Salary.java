package com.mfvanek.salary.calc.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "salary_calc")
public class Salary {

    @Id
    @NotNull
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "calc_date", nullable = false)
    private LocalDate calculationDate;

    @NotNull
    @Column(name = "wrk_days", nullable = false)
    private int workingDaysCount;

    @NotNull
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employeeId;

    @OneToOne(mappedBy = "salaryId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Ticket ticket;

    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 41)
                .append(id)
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof Salary)) {
            return false;
        }

        Salary other = (Salary) o;
        return new EqualsBuilder()
                .append(this.id, other.id)
                .isEquals();
    }
//
//    public void setEmployeeId(final Employee employee) {
//        Objects.requireNonNull(employee);
//        if (this.employeeId != null && this.employeeId != employee) {
//            throw new IllegalStateException("EmployeeId is already set");
//        }
//
//        this.employeeId = employee;
//    }
}
