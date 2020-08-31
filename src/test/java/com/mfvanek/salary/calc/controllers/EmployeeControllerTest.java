package com.mfvanek.salary.calc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfvanek.salary.calc.dtos.EmployeeDto;
import com.mfvanek.salary.calc.requests.EmployeeCreationRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.HeaderResultMatchers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

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
        assertNotNull(mvcResult);

        final MockHttpServletResponse resultResponse = mvcResult.getResponse();
        assertNotNull(resultResponse);

        final EmployeeDto employeeDto = objectMapper.readValue(resultResponse.getContentAsString(), EmployeeDto.class);
        assertNotNull(employeeDto);
        assertEquals("John", employeeDto.getFirstName());
        assertEquals("Doe", employeeDto.getLastName());
        assertEquals(8, employeeDto.getStandardHoursPerDay());
        assertThat(employeeDto.getSalaryPerHour(), comparesEqualTo(BigDecimal.valueOf(400L)));

        final String locationHeader = resultResponse.getHeader(HttpHeaders.LOCATION);
        assertNotNull(locationHeader);

        mockMvc.perform(get(locationHeader)
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
}
