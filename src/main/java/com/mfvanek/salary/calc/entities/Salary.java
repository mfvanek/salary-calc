package com.mfvanek.salary.calc.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
@Table(name = "salary_calc", indexes = {
    @Index(name = "idx_salary_calc_emp_id", columnList = "emp_id")
})
@Comment("Table for storing calculated salary")
public class Salary extends BaseEntity {

    @NotNull
    @Column(name = "calculated_at", nullable = false, columnDefinition = "timestamptz")
    private ZonedDateTime calculationDate;

    @Column(name = "wrk_days", nullable = false)
    private int workingDaysCount;

    @NotNull
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employeeId;

    @OneToOne(mappedBy = "salaryId", cascade = CascadeType.ALL)
    private Ticket ticket;

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof final Salary other)) {
            return false;
        }

        return Objects.equals(this.getId(), other.getId());
    }
}
