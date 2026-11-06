package com.mfvanek.salary.calc.entities;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.ZonedDateTime;

// Constructor injection is required: field injection doesn't work in native image
@Component
@RequiredArgsConstructor
public class ClockAwareEntityListener {

    private final Clock clock;

    @PrePersist
    public void initCreatedAt(@NonNull final BaseEntity entity) {
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(ZonedDateTime.now(clock));
        }
    }

    @PreUpdate
    public void initUpdatedAt(@NonNull final BaseEntity entity) {
        if (entity.getId() != null && entity.getCreatedAt() != null) {
            entity.setUpdatedAt(ZonedDateTime.now(clock));
        }
    }
}
