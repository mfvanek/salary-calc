package com.mfvanek.salary.calc;

import com.mfvanek.salary.calc.config.ClockHolder;
import com.mfvanek.salary.calc.support.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
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
        assertThat(context.getBean("clock"))
                .isNotNull()
                .isInstanceOf(Clock.class);
        assertThat(context.getBean("clockProvider"))
                .isNotNull()
                .isInstanceOf(ClockProvider.class);
    }

    @Test
    void clockShouldBeFixed() {
        final LocalDateTime realNow = LocalDateTime.now(Clock.systemDefaultZone());

        assertThat(LocalDateTime.now(clock))
                .isBefore(realNow)
                .isEqualTo(LocalDateTime.of(1999, Month.DECEMBER, 31, 23, 59, 59));

        assertThat(LocalDateTime.now(clockProvider.getClock()))
                .isBefore(realNow)
                .isEqualTo(LocalDateTime.of(1999, Month.DECEMBER, 31, 23, 59, 59));
    }

    @Test
    void clockCanBeChangedLocallyButClockBeanRemainsOld() {
        final LocalDateTime distantFuture = LocalDateTime.of(3000, Month.JANUARY, 1, 0, 0, 0);
        final Clock fixed = Clock.fixed(distantFuture.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        final Clock oldClock = ClockHolder.setClock(fixed);
        try {
            final LocalDateTime realNow = LocalDateTime.now(Clock.systemDefaultZone());

            // clock bean hasn't changed!
            assertThat(LocalDateTime.now(clock))
                    .isBefore(realNow)
                    .isEqualTo(LocalDateTime.of(1999, Month.DECEMBER, 31, 23, 59, 59));

            assertThat(LocalDateTime.now(clockProvider.getClock()))
                    .isAfter(realNow)
                    .isEqualTo(LocalDateTime.of(3000, Month.JANUARY, 1, 0, 0, 0));
        } finally {
            ClockHolder.setClock(oldClock);
        }
    }
}
