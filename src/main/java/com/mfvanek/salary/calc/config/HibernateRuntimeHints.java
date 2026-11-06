package com.mfvanek.salary.calc.config;

import org.jspecify.annotations.Nullable;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

import java.util.UUID;

/**
 * Reflection hints that GraalVM native image needs but that Hibernate/Spring do not register on their own.
 */
public class HibernateRuntimeHints implements RuntimeHintsRegistrar {

    /**
     * Hibernate instantiates the binder behind {@code @Comment} reflectively.
     */
    static final TypeReference COMMENT_BINDER = TypeReference.of("org.hibernate.binder.internal.CommentBinder");

    /**
     * Hibernate's multi-id loader reflectively instantiates an array of the entity identifier type.
     * All entities use {@link UUID} identifiers (see {@code BaseEntity}).
     */
    static final TypeReference ID_ARRAY = TypeReference.of(UUID[].class);

    @Override
    public void registerHints(final RuntimeHints hints, @Nullable final ClassLoader classLoader) {
        hints.reflection()
            .registerType(COMMENT_BINDER, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS)
            .registerType(ID_ARRAY);
    }
}
