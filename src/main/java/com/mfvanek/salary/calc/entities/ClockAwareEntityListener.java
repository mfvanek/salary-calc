package com.mfvanek.salary.calc.entities;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.ZonedDateTime;

@Component
@NoArgsConstructor
public class ClockAwareEntityListener {

    // Couldn't use constructor injection here
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private Clock clock;

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
