package com.mfvanek.salary.calc.repositories;

import com.mfvanek.salary.calc.support.TestBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.QueryTimeoutException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestPropertySource("/application-test.yml")
class JpaPropertiesTest extends TestBase {

    @Autowired
    EntityManager entityManager;

    @Test
    void failsWhenQueryTooLong() {
        final Query query = entityManager
                .createNativeQuery(
                        "SELECT pg_sleep(5)");
        assertThatThrownBy(query::getResultList)
                .isNotNull()
                .isInstanceOf(QueryTimeoutException.class);
    }

    @Test
    @DisplayName("Does not throw exception when query does not exceed timeout")
    void exceptionWithNotLongQuery() {
        final Query query = entityManager
                .createNativeQuery(
                        "SELECT pg_sleep(3.9)");
        assertThatNoException().isThrownBy(query::getResultList);
    }
}


