package com.mfvanek.salary.calc;

import com.mfvanek.salary.calc.support.TestBase;
import io.github.mfvanek.pg.common.maintenance.DatabaseCheckOnHost;
import io.github.mfvanek.pg.common.maintenance.Diagnostic;
import io.github.mfvanek.pg.model.DbObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class IndexesMaintenanceTest extends TestBase {

    @Autowired
    private List<DatabaseCheckOnHost<? extends DbObject>> checks;

    @Test
    @DisplayName("Always check PostgreSQL version in your tests")
    void checkPostgresVersion() {
        final String pgVersion = jdbcTemplate.queryForObject("select version();", String.class);
        assertThat(pgVersion)
                .startsWith("PostgreSQL 16.4");
    }

    @Test
    void databaseStructureCheckForPublicSchema() {
        assertThat(checks)
                .hasSameSizeAs(Diagnostic.values());

        checks.forEach(check -> {
            if (check.getDiagnostic() == Diagnostic.COLUMNS_WITHOUT_DESCRIPTION) {
                assertThat(check.check())
                        .hasSize(22);
            } else if (check.getDiagnostic() == Diagnostic.TABLES_WITH_MISSING_INDEXES) {
                assertThat(check.check())
                        .hasSizeLessThanOrEqualTo(1); // TODO skip runtime checks after https://github.com/mfvanek/pg-index-health/issues/456
            } else {
                assertThat(check.check())
                        .as(check.getDiagnostic().name())
                        .isEmpty();
            }
        });
    }
}
