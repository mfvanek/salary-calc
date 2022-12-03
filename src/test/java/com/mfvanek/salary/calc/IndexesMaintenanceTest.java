package com.mfvanek.salary.calc;

import com.mfvanek.salary.calc.support.TestBase;
import io.github.mfvanek.pg.checks.host.ColumnsWithJsonTypeCheckOnHost;
import io.github.mfvanek.pg.checks.host.ColumnsWithSerialTypesCheckOnHost;
import io.github.mfvanek.pg.checks.host.ColumnsWithoutDescriptionCheckOnHost;
import io.github.mfvanek.pg.checks.host.DuplicatedIndexesCheckOnHost;
import io.github.mfvanek.pg.checks.host.ForeignKeysNotCoveredWithIndexCheckOnHost;
import io.github.mfvanek.pg.checks.host.IndexesWithNullValuesCheckOnHost;
import io.github.mfvanek.pg.checks.host.IntersectedIndexesCheckOnHost;
import io.github.mfvanek.pg.checks.host.InvalidIndexesCheckOnHost;
import io.github.mfvanek.pg.checks.host.TablesWithoutDescriptionCheckOnHost;
import io.github.mfvanek.pg.checks.host.TablesWithoutPrimaryKeyCheckOnHost;
import io.github.mfvanek.pg.model.index.IndexWithNulls;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class IndexesMaintenanceTest extends TestBase {

    @Autowired
    private InvalidIndexesCheckOnHost invalidIndexesCheck;
    @Autowired
    private DuplicatedIndexesCheckOnHost duplicatedIndexesCheck;
    @Autowired
    private IntersectedIndexesCheckOnHost intersectedIndexesCheck;
    @Autowired
    private ForeignKeysNotCoveredWithIndexCheckOnHost foreignKeysNotCoveredWithIndexCheck;
    @Autowired
    private TablesWithoutPrimaryKeyCheckOnHost tablesWithoutPrimaryKeyCheck;
    @Autowired
    private IndexesWithNullValuesCheckOnHost indexesWithNullValuesCheck;
    @Autowired
    private TablesWithoutDescriptionCheckOnHost tablesWithoutDescriptionCheck;
    @Autowired
    private ColumnsWithoutDescriptionCheckOnHost columnsWithoutDescriptionCheck;
    @Autowired
    private ColumnsWithJsonTypeCheckOnHost columnsWithJsonTypeCheck;
    @Autowired
    private ColumnsWithSerialTypesCheckOnHost columnsWithSerialTypesCheck;

    @Test
    @DisplayName("Always check PostgreSQL version in your tests")
    void checkPostgresVersion() {
        final String pgVersion = jdbcTemplate.queryForObject("select version();", String.class);
        assertThat(pgVersion)
                .startsWith("PostgreSQL 14.5");
    }

    @Test
    void getInvalidIndexesShouldReturnNothing() {
        assertThat(invalidIndexesCheck.check())
                .isEmpty();
    }

    @Test
    void getDuplicatedIndexesShouldReturnNothing() {
        assertThat(duplicatedIndexesCheck.check())
                .isEmpty();
    }

    @Test
    void getIntersectedIndexesShouldReturnNothing() {
        assertThat(intersectedIndexesCheck.check())
                .isEmpty();
    }

    @Test
    void getForeignKeysNotCoveredWithIndexShouldReturnNothing() {
        assertThat(foreignKeysNotCoveredWithIndexCheck.check())
                .isEmpty();
    }

    @Test
    void getTablesWithoutPrimaryKeyShouldReturnNothing() {
        assertThat(tablesWithoutPrimaryKeyCheck.check())
                .isEmpty();
    }

    @Test
    void getIndexesWithNullValuesShouldReturnOneRow() {
        assertThat(indexesWithNullValuesCheck.check())
                .hasSize(1)
                .containsExactly(
                        IndexWithNulls.of("tickets", "idx_tickets_salary_id", 8_192L, "salary_id")
                );
    }

    @Test
    void getTablesWithoutDescriptionShouldReturnNothing() {
        assertThat(tablesWithoutDescriptionCheck.check())
                .isEmpty();
    }

    @Test
    void getColumnsWithoutDescriptionShouldReturnSeveralRows() {
        assertThat(columnsWithoutDescriptionCheck.check())
                .hasSize(22);
    }

    @Test
    void getColumnsWithJsonTypeShouldReturnNothing() {
        assertThat(columnsWithJsonTypeCheck.check())
                .isEmpty();
    }

    @Test
    void getColumnsWithSerialTypesShouldReturnNothing() {
        assertThat(columnsWithSerialTypesCheck.check())
                .isEmpty();
    }
}
