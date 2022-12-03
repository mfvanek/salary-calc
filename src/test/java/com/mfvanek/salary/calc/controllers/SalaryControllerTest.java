package com.mfvanek.salary.calc.controllers;

import com.mfvanek.salary.calc.dtos.SalaryDto;
import com.mfvanek.salary.calc.support.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SalaryControllerTest extends TestBase {

    @Test
    void getSalaryCalculationShouldReturnNotFoundOnEmptyDatabase() {
        final SalaryDto salary = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/salary/{id}")
                        .build(UUID.randomUUID()))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(SalaryDto.class)
                .returnResult()
                .getResponseBody();
        assertThat(salary)
                .isNotNull()
                .isEqualTo(SalaryDto.builder().build());
    }
}
