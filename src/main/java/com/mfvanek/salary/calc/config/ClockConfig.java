package com.mfvanek.salary.calc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import javax.validation.ClockProvider;

@Configuration(proxyBeanMethods = false)
public class ClockConfig {

    @Bean
    public Clock clock() {
        return ClockHolder.getClock();
    }

    @Bean
    public ClockProvider clockProvider() {
        return ClockHolder::getClock;
    }
}
