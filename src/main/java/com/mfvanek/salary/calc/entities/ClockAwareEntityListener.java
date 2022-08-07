package com.mfvanek.salary.calc.entities;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import javax.annotation.Nonnull;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Component
@NoArgsConstructor
public class ClockAwareEntityListener {

    // Couldn't use constructor injection here
    @Autowired
    private Clock clock;

    @PrePersist
    public void initCreatedAt(@Nonnull final BaseEntity entity) {
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(LocalDateTime.now(clock));
        }
    }

    @PreUpdate
    public void initUpdatedAt(@Nonnull final BaseEntity entity) {
        if (entity.getId() != null && entity.getCreatedAt() != null) {
            entity.setUpdatedAt(LocalDateTime.now(clock));
        }
    }
}
