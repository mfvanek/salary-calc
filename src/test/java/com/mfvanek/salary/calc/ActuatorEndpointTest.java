/*
 * Copyright (c) 2019-2023. Ivan Vakhrushev and others.
 * https://github.com/mfvanek/pg-index-health-spring-boot-demo
 *
 * Licensed under the Apache License 2.0
 */

package com.mfvanek.salary.calc;

import com.mfvanek.salary.calc.support.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.annotation.Nonnull;

import static org.assertj.core.api.Assertions.assertThat;

class ActuatorEndpointTest extends TestBase {

    private WebTestClient actuatorClient;

    @BeforeEach
    void setUp() {
        this.actuatorClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + actuatorPort + "/actuator/")
                .build();
    }

    @Test
    void actuatorShouldBeRunOnSeparatePort() {
        assertThat(actuatorPort)
                .isNotEqualTo(port);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "prometheus|jvm_threads_live_threads|text/plain",
            "health|{\"status\":\"UP\",\"groups\":[\"liveness\",\"readiness\"]}|application/json",
            "health/liveness|{\"status\":\"UP\"}|application/json",
            "health/readiness|{\"status\":\"UP\"}|application/json",
            "openapi/springdocDefault|{\"openapi\":\"3.0.1\",\"info\":{\"title\":\"salary-calc\"|application/json",
            "openapi/x-actuator|{\"openapi\":\"3.0.1\",\"info\":{\"title\":\"salary-calc\"|application/json",
            "info|\"version\":|application/json"}, delimiter = '|')
    void actuatorEndpointShouldReturnOk(@Nonnull final String endpointName,
                                        @Nonnull final String expectedSubstring,
                                        @Nonnull final String mediaType) {
        final String result = actuatorClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(endpointName)
                        .build())
                .accept(MediaType.valueOf(mediaType))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();
        assertThat(result)
                .contains(expectedSubstring);
    }

    @Test
    void swaggerUiEndpointShouldReturnFound() {
        final byte[] result = actuatorClient.get()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment("swagger-ui")
                        .build())
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().location("/actuator/swagger-ui/index.html")
                .expectBody()
                .returnResult()
                .getResponseBody();
        assertThat(result).isNull();
    }

    @Test
    void readinessProbeShouldBeCollectedFromApplicationMainPort() {
        final String result = webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment("readyz")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();
        assertThat(result)
                .isEqualTo("{\"status\":\"UP\"}");

        final String metricsResult = actuatorClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("prometheus")
                        .build())
                .accept(MediaType.valueOf("text/plain"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();
        assertThat(metricsResult)
                .contains("http_server_requests_seconds_bucket");
    }

    @Test
    void openapiEndpointNotFound() {
        final String result = actuatorClient.get()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment("openapi")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();
        assertThat(result)
                .contains("\"status\":404,\"error\":\"Not Found\",\"path\":\"/actuator/openapi\"");
    }
}
