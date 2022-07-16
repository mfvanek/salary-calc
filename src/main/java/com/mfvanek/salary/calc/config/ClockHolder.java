package com.mfvanek.salary.calc.config;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nonnull;

@Slf4j
@UtilityClass
public final class ClockHolder {

    private static final AtomicReference<Clock> CLOCK_REFERENCE = new AtomicReference<>(Clock.systemDefaultZone());

    @Nonnull
    public static Clock getClock() {
        return CLOCK_REFERENCE.get();
    }

    /**
     * Atomically sets the value to {@code newClock} and returns the old value.
     *
     * @param newClock the new value
     * @return the previous value of clock
     */
    @Nonnull
    public static Clock setClock(@Nonnull final Clock newClock) {
        Objects.requireNonNull(newClock, "newClock cannot be null");
        final Clock oldClock = CLOCK_REFERENCE.getAndSet(newClock);
        log.info("Set new clock {}. Old clock is {}", newClock, oldClock);
        return oldClock;
    }
}
