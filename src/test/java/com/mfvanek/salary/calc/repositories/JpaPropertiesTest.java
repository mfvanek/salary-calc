package com.mfvanek.salary.calc.repositories;

import com.mfvanek.salary.calc.support.TestBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import org.hibernate.QueryTimeoutException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JpaPropertiesTest extends TestBase {

    @Autowired
    EntityManager entityManager;

    /**
     * Since Hibernate 7.4, a query timeout is reported as a plain {@link PersistenceException}
     * (instead of {@link jakarta.persistence.QueryTimeoutException}) when the database marks
     * the whole transaction for rollback, which is always the case for PostgreSQL.
     * The original Hibernate {@link QueryTimeoutException} is still available as the cause.
     */
    @Test
    void failsWhenQueryTooLong() {
        final Query query = entityManager
            .createNativeQuery(
                "SELECT pg_sleep(2)");
        assertThatThrownBy(query::getResultList)
            .isExactlyInstanceOf(PersistenceException.class)
            .hasMessageContaining("ERROR: canceling statement due to user request")
            .cause()
            .isInstanceOf(QueryTimeoutException.class);
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
