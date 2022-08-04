package com.mfvanek.salary.calc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfvanek.salary.calc.dtos.EmployeeDto;
import com.mfvanek.salary.calc.requests.EmployeeCreationRequest;
import com.mfvanek.salary.calc.support.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EmployeeControllerTest extends TestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getEmployeeShouldReturnNotFoundForRandomId() throws Exception {
        mockMvc.perform(get("/employee/{id}", UUID.randomUUID())
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createEmployee() throws Exception {
        final EmployeeCreationRequest employeeCreationRequest = new EmployeeCreationRequest();
        employeeCreationRequest.setFirstName("John");
        employeeCreationRequest.setLastName("Doe");
        employeeCreationRequest.setStandardHoursPerDay(8);
        employeeCreationRequest.setSalaryPerHour(BigDecimal.valueOf(400L));

        final MvcResult mvcResult = mockMvc.perform(post("/employee")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(employeeCreationRequest)))
                .andExpect(status().isCreated())
                .andReturn();
        assertThat(mvcResult)
                .isNotNull();

        final MockHttpServletResponse resultResponse = mvcResult.getResponse();
        assertThat(resultResponse)
                .isNotNull();

        final EmployeeDto employeeDto = objectMapper.readValue(resultResponse.getContentAsString(), EmployeeDto.class);
        assertThat(employeeDto)
                .isNotNull()
                .satisfies(e -> {
                    assertThat(e.getFirstName())
                            .isEqualTo("John");
                    assertThat(e.getLastName())
                            .isEqualTo("Doe");
                    assertThat(e.getStandardHoursPerDay())
                            .isEqualTo(8);
                    assertThat(e.getSalaryPerHour())
                            .isEqualByComparingTo(BigDecimal.valueOf(400L));
                });

        final String locationHeader = resultResponse.getHeader(HttpHeaders.LOCATION);
        assertThat(locationHeader)
                .isNotNull();

        mockMvc.perform(get(locationHeader).contentType("application/json"))
                .andExpect(status().isOk());
    }
}
