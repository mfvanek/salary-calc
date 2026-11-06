package com.mfvanek.salary.calc.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.predicate.RuntimeHintsPredicates;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class HibernateRuntimeHintsTest {

    private final RuntimeHints hints = new RuntimeHints();

    @BeforeEach
    void registerHints() {
        new HibernateRuntimeHints().registerHints(hints, Thread.currentThread().getContextClassLoader());
    }

    @Test
    void shouldRegisterConstructorOfCommentBinder() {
        assertThat(RuntimeHintsPredicates.reflection()
            .onType(HibernateRuntimeHints.COMMENT_BINDER)
            .withMemberCategory(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS))
            .accepts(hints);
    }

    @Test
    void shouldRegisterArrayOfIdentifierType() {
        assertThat(RuntimeHintsPredicates.reflection().onType(UUID[].class))
            .accepts(hints);
    }
}
