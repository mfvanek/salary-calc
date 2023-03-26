package com.mfvanek.salary.calc.controllers;

import com.mfvanek.salary.calc.dtos.EmployeeDto;
import com.mfvanek.salary.calc.requests.EmployeeCreationRequest;
import com.mfvanek.salary.calc.support.TestBase;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeControllerTest extends TestBase {

    @Test
    void getEmployeeShouldReturnNotFoundForRandomId() {
        final var result = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/employee/{id}")
                        .build(UUID.randomUUID()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(EmployeeDto.class)
                .returnResult()
                .getResponseBody();
        assertThat(result)
                .isEqualTo(new EmployeeDto());
    }

    @Test
    void createEmployee() {
        final var employeeCreationRequest = EmployeeCreationRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .standardHoursPerDay(8)
                .salaryPerHour(BigDecimal.valueOf(400L))
                .build();

        final var result = webTestClient.post()
                .uri("/employee")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employeeCreationRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(EmployeeDto.class)
                .returnResult();

        assertThat(result)
                .isNotNull();
        assertThat(result.getResponseHeaders())
                .hasSizeGreaterThanOrEqualTo(2)
                .containsKeys(HttpHeaders.CONTENT_TYPE, HttpHeaders.LOCATION);

        final List<String> location = result.getResponseHeaders().get(HttpHeaders.LOCATION);
        assertThat(location)
                .isNotNull()
                .hasSize(1)
                .first(InstanceOfAssertFactories.STRING)
                .startsWith("http://localhost:%d/api/employee/".formatted(port));

        final var createdEmployee = result.getResponseBody();
        assertThat(createdEmployee)
                .isNotNull()
                .satisfies(e -> {
                    assertThat(e.getId())
                            .isNotNull();
                    assertThat(e.getFirstName())
                            .isEqualTo("John");
                    assertThat(e.getLastName())
                            .isEqualTo("Doe");
                    assertThat(e.getStandardHoursPerDay())
                            .isEqualTo(8);
                    assertThat(e.getSalaryPerHour())
                            .isEqualByComparingTo("400.00");
                });

        final var foundEmployee = webTestClient.get()
                .uri(location.get(0))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(foundEmployee)
                .isNotNull()
                .satisfies(e -> assertThat(e.getId())
                        .isEqualTo(createdEmployee.getId()));
    }
}
