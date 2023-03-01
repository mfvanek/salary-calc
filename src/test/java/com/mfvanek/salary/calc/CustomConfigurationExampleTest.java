package com.mfvanek.salary.calc;

import com.mfvanek.salary.calc.config.ClockConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(classes = {ClockConfig.class, CustomConfigurationExampleTest.CustomClockConfiguration.class})
class CustomConfigurationExampleTest {

    private static final LocalDateTime MILLENNIUM = LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0, 0);

    @Autowired
    private Clock clock;

    @Test
    void clockAlsoShouldBeFixed() {
        assertThat(LocalDateTime.now(clock))
                .isBefore(LocalDateTime.now(Clock.systemUTC()))
                .isEqualTo(LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0, 0));
    }

    @TestConfiguration
    static class CustomClockConfiguration {

        @Bean
        @Primary
        public Clock fixedClock() {
            return Clock.fixed(MILLENNIUM.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        }
    }
}
