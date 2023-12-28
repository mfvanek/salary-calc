package com.mfvanek.salary.calc.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
@Table(name = "employees")
@Comment("Table for storing employees data")
public class Employee extends BaseEntity {

    @NotNull
    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Min(1)
    @Max(8)
    @Column(name = "hours_per_day", nullable = false)
    private int standardHoursPerDay;

    @NotNull
    @DecimalMax("5000.00")
    @DecimalMin("100.00")
    @Column(name = "salary_per_hour", nullable = false)
    private BigDecimal salaryPerHour;

    @Builder.Default
    @JsonIgnore
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId")
    private Set<Salary> salaries = new HashSet<>();

    @Builder.Default
    @JsonIgnore
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId")
    private Set<Ticket> tickets = new HashSet<>();

    @Nonnull
    public Employee withSalary(@Nonnull final Salary salary) {
        salaries.add(salary);
        salary.setEmployeeId(this);
        return this;
    }
}
