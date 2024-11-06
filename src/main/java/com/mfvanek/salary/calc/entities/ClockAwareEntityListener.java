package com.mfvanek.salary.calc.entities;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.ZonedDateTime;
import javax.annotation.Nonnull;

@Component
@NoArgsConstructor
public class ClockAwareEntityListener {

    // Couldn't use constructor injection here
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private Clock clock;

    @PrePersist
    public void initCreatedAt(@Nonnull final BaseEntity entity) {
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(ZonedDateTime.now(clock));
        }
    }

    @PreUpdate
    public void initUpdatedAt(@Nonnull final BaseEntity entity) {
        if (entity.getId() != null && entity.getCreatedAt() != null) {
            entity.setUpdatedAt(ZonedDateTime.now(clock));
        }
    }
}
