package com.mfvanek.salary.calc.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @NotNull
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @NotBlank
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @NotBlank
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Min(value = 1)
    @Max(value = 8)
    @Column(name = "hours_per_day", nullable = false)
    private int standardHoursPerDay;

    @NotNull
    @DecimalMax(value = "5000.00")
    @DecimalMin(value = "100.00")
    @Column(name = "salary_per_hour", nullable = false)
    private BigDecimal salaryPerHour;

    @JsonIgnore
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId")
    private Set<Salary> salaries = new HashSet<>();

    @JsonIgnore
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId")
    private Set<Ticket> tickets = new HashSet<>();
}
