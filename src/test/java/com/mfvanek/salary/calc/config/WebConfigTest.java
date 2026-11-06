package com.mfvanek.salary.calc.config;

import com.mfvanek.salary.calc.support.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static org.assertj.core.api.Assertions.assertThat;

class WebConfigTest extends TestBase {

    @Test
    void shouldAllowCrossOriginRequestsFromSwaggerUi() {
        final EntityExchangeResult<byte[]> result = getAllEmployeesFrom("http://localhost:8090");

        assertThat(result.getStatus())
            .isEqualTo(HttpStatus.OK);
        assertThat(result.getResponseHeaders().getAccessControlAllowOrigin())
            .isEqualTo(WebConfig.SWAGGER_UI_ORIGIN);
    }

    @Test
    void shouldRejectCrossOriginRequestsFromOtherOrigins() {
        final EntityExchangeResult<byte[]> result = getAllEmployeesFrom("http://evil.example.com");

        assertThat(result.getStatus())
            .isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(result.getResponseHeaders().getAccessControlAllowOrigin())
            .isNull();
    }

    private EntityExchangeResult<byte[]> getAllEmployeesFrom(final String origin) {
        return webTestClient.get()
            .uri("/employee/all")
            .header(HttpHeaders.ORIGIN, origin)
            .exchange()
            .expectBody()
            .returnResult();
    }
}
