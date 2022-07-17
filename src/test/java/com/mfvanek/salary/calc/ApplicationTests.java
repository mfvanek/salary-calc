package com.mfvanek.salary.calc;

import com.mfvanek.salary.calc.support.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTests extends TestBase {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        assertThat(context.containsBean("clock"))
                .isTrue();
        assertThat(context.containsBean("clockProvider"))
                .isTrue();
    }
}
