package com.mfvanek.salary.calc;

import com.mfvanek.salary.calc.support.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import javax.validation.ClockProvider;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTests extends TestBase {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private Clock clock;
    @Autowired
    private ClockProvider clockProvider;

    @Test
    void contextLoads() {
        assertThat(context.containsBean("clock"))
                .isTrue();
        assertThat(context.containsBean("clockProvider"))
                .isTrue();
    }

    @Test
    void clockShouldBeFixed() {
        final LocalDateTime realNow = LocalDateTime.now();

        assertThat(LocalDateTime.now(clock))
                .isBefore(realNow)
                .isEqualTo(LocalDateTime.of(1999, Month.DECEMBER, 31, 23, 59, 59));

        assertThat(LocalDateTime.now(clockProvider.getClock()))
                .isBefore(realNow)
                .isEqualTo(LocalDateTime.of(1999, Month.DECEMBER, 31, 23, 59, 59));
    }
}
