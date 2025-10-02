package com.mfvanek.salary.calc;

import com.mfvanek.salary.calc.support.TestBase;
import io.github.mfvanek.pg.core.checks.common.DatabaseCheckOnHost;
import io.github.mfvanek.pg.core.checks.common.Diagnostic;
import io.github.mfvanek.pg.model.column.Column;
import io.github.mfvanek.pg.model.dbobject.DbObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;

class IndexesMaintenanceTest extends TestBase {

    @Autowired
    private List<DatabaseCheckOnHost<? extends DbObject>> checks;

    @Test
    @DisplayName("Always check PostgreSQL version in your tests")
    void checkPostgresVersion() {
        final String pgVersion = jdbcTemplate.queryForObject("select version();", String.class);
        assertThat(pgVersion)
            .startsWith("PostgreSQL 17.6");
    }

    @Test
    void databaseStructureCheckForPublicSchema() {
        assertThat(checks)
            .hasSameSizeAs(Diagnostic.values());

        checks.stream()
            .filter(DatabaseCheckOnHost::isStatic)
            .forEach(check -> {
                if (check.getDiagnostic() == Diagnostic.COLUMNS_WITHOUT_DESCRIPTION) {
                    assertThat(check.check())
                        .hasSize(22);
                } else if (check.getDiagnostic() == Diagnostic.COLUMNS_WITH_FIXED_LENGTH_VARCHAR) {
                    assertThat(check.check())
                        .asInstanceOf(list(Column.class))
                        .hasSize(3)
                        .containsExactly(
                            Column.ofNotNull("employees", "first_name"),
                            Column.ofNotNull("employees", "last_name"),
                            Column.ofNotNull("tickets", "calc_params")
                        );
                } else {
                    assertThat(check.check())
                        .as(check.getDiagnostic().name())
                        .isEmpty();
                }
            });
    }
}
