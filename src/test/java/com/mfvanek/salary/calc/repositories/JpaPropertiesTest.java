package com.mfvanek.salary.calc.repositories;

import com.mfvanek.salary.calc.support.TestBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.QueryTimeoutException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JpaPropertiesTest extends TestBase {

    @Autowired
    EntityManager entityManager;

    @Test
    void failsWhenQueryTooLong() {
        final Query query = entityManager
                .createNativeQuery(
                        "SELECT pg_sleep(2)");
        assertThatThrownBy(query::getResultList)
                .isInstanceOf(QueryTimeoutException.class)
                .hasMessageContaining("ERROR: canceling statement due to user request");
    }

    @Test
    @DisplayName("Does not throw exception when query does not exceed timeout")
    void exceptionWithNotLongQuery() {
        final Query query = entityManager
                .createNativeQuery(
                        "SELECT pg_sleep(0.9)");
        assertThatNoException().isThrownBy(query::getResultList);
    }
}
