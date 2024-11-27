package com.mfvanek.salary.calc.controllers;

import com.mfvanek.salary.calc.dtos.TicketDto;
import com.mfvanek.salary.calc.support.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TicketControllerTest extends TestBase {

    @Test
    void getTicketShouldReturnNotFoundForUnknownId() {
        final TicketDto ticket = webTestClient.get()
            .uri(uriBuilder -> uriBuilder.path("/ticket/{id}")
                .build(UUID.randomUUID()))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(TicketDto.class)
            .returnResult()
            .getResponseBody();
        assertThat(ticket)
            .isNotNull()
            .isEqualTo(TicketDto.builder().build());
    }
}
