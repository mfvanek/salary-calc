package com.mfvanek.salary.calc;

import com.mfvanek.salary.calc.support.PostgresInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(classes = {CustomConfigurationExampleTest.CustomClockConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {PostgresInitializer.class})
class CustomConfigurationExampleTest {

    private static final LocalDateTime MILLENNIUM = LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0, 0);

    @Autowired
    private Clock clock;

    @TestConfiguration
    static class CustomClockConfiguration {

        @Bean
        @Primary
        public Clock fixedClock() {
            return Clock.fixed(MILLENNIUM.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        }
    }

    @Test
    void clockAlsoShouldBeFixed() {
        final LocalDateTime realNow = LocalDateTime.now(Clock.systemDefaultZone());

        assertThat(LocalDateTime.now(clock))
                .isBefore(realNow)
                .isEqualTo(LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0, 0));
    }
}
