package com.mfvanek.salary.calc;

import com.mfvanek.salary.calc.support.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTests extends TestBase {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        assertThat(context.getBean("clock"))
                .isNotNull()
                .isInstanceOf(Clock.class);
    }

    @Test
    void clockShouldBeFixed() {
        assertThat(LocalDateTime.now(clock))
                .isBefore(LocalDateTime.now(Clock.systemUTC()))
                .isEqualTo(LocalDateTime.of(1999, Month.DECEMBER, 31, 23, 59, 59));
    }

    @Test
    void clockCanBeChangedLocally() {
        mutableClock.add(1_000L, ChronoUnit.YEARS);

        assertThat(LocalDateTime.now(clock))
                .isAfter(LocalDateTime.now(Clock.systemUTC()))
                .isEqualTo(LocalDateTime.of(2999, Month.DECEMBER, 31, 23, 59, 59));
    }
}
