package com.mfvanek.salary.calc;

import com.mfvanek.salary.calc.support.TestBase;
import io.github.mfvanek.pg.core.checks.common.DatabaseCheckOnHost;
import io.github.mfvanek.pg.core.checks.common.Diagnostic;
import io.github.mfvanek.pg.model.column.Column;
import io.github.mfvanek.pg.model.dbobject.DbObject;
import io.github.mfvanek.pg.model.table.Table;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;

class DatabaseStructureStaticAnalysisTest extends TestBase {

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
                final ListAssert<? extends DbObject> listAssert = assertThat(check.check())
                    .as(check.getDiagnostic().name());

                switch (check.getDiagnostic()) {
                    case Diagnostic.COLUMNS_WITHOUT_DESCRIPTION -> listAssert.hasSize(22);

                    case Diagnostic.COLUMNS_WITH_FIXED_LENGTH_VARCHAR -> listAssert
                        .asInstanceOf(list(Column.class))
                        .hasSize(3)
                        .containsExactly(
                            Column.ofNotNull("employees", "first_name"),
                            Column.ofNotNull("employees", "last_name"),
                            Column.ofNotNull("tickets", "calc_params")
                        );

                    case Diagnostic.TABLES_WHERE_PRIMARY_KEY_COLUMNS_NOT_FIRST -> listAssert
                        .asInstanceOf(list(Table.class))
                        .hasSize(3)
                        .containsExactly(
                            Table.of("employees"),
                            Table.of("salary_calc"),
                            Table.of("tickets")
                        );

                    default -> listAssert.isEmpty();
                }
            });
    }
}
