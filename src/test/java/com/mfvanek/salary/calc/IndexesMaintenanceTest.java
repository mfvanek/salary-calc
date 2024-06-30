package com.mfvanek.salary.calc;

import com.mfvanek.salary.calc.support.TestBase;
import io.github.mfvanek.pg.checks.host.BtreeIndexesOnArrayColumnsCheckOnHost;
import io.github.mfvanek.pg.checks.host.ColumnsWithJsonTypeCheckOnHost;
import io.github.mfvanek.pg.checks.host.ColumnsWithSerialTypesCheckOnHost;
import io.github.mfvanek.pg.checks.host.ColumnsWithoutDescriptionCheckOnHost;
import io.github.mfvanek.pg.checks.host.DuplicatedIndexesCheckOnHost;
import io.github.mfvanek.pg.checks.host.ForeignKeysNotCoveredWithIndexCheckOnHost;
import io.github.mfvanek.pg.checks.host.FunctionsWithoutDescriptionCheckOnHost;
import io.github.mfvanek.pg.checks.host.IndexesWithBooleanCheckOnHost;
import io.github.mfvanek.pg.checks.host.IndexesWithNullValuesCheckOnHost;
import io.github.mfvanek.pg.checks.host.IntersectedIndexesCheckOnHost;
import io.github.mfvanek.pg.checks.host.InvalidIndexesCheckOnHost;
import io.github.mfvanek.pg.checks.host.NotValidConstraintsCheckOnHost;
import io.github.mfvanek.pg.checks.host.PrimaryKeysWithSerialTypesCheckOnHost;
import io.github.mfvanek.pg.checks.host.SequenceOverflowCheckOnHost;
import io.github.mfvanek.pg.checks.host.TablesWithoutDescriptionCheckOnHost;
import io.github.mfvanek.pg.checks.host.TablesWithoutPrimaryKeyCheckOnHost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "PMD.TooManyFields"})
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
    @Autowired
    private FunctionsWithoutDescriptionCheckOnHost functionsWithoutDescriptionCheck;
    @Autowired
    private IndexesWithBooleanCheckOnHost indexesWithBooleanCheck;
    @Autowired
    private NotValidConstraintsCheckOnHost notValidConstraintsCheck;
    @Autowired
    private BtreeIndexesOnArrayColumnsCheckOnHost btreeIndexesOnArrayColumnsCheck;
    @Autowired
    private SequenceOverflowCheckOnHost sequenceOverflowCheck;
    @Autowired
    private PrimaryKeysWithSerialTypesCheckOnHost primaryKeysWithSerialTypesCheck;

    @Test
    @DisplayName("Always check PostgreSQL version in your tests")
    void checkPostgresVersion() {
        final String pgVersion = jdbcTemplate.queryForObject("select version();", String.class);
        assertThat(pgVersion)
                .startsWith("PostgreSQL 16.2");
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
    void getIndexesWithNullValuesShouldReturnNothing() {
        assertThat(indexesWithNullValuesCheck.check())
                .isEmpty();
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

    @Test
    void getFunctionsWithoutDescriptionShouldReturnNothing() {
        assertThat(functionsWithoutDescriptionCheck.check())
                .isEmpty();
    }

    @Test
    void indexesWithBooleanShouldReturnNothing() {
        assertThat(indexesWithBooleanCheck.check())
                .isEmpty();
    }

    @Test
    void notValidConstraintsShouldReturnNothing() {
        assertThat(notValidConstraintsCheck.check())
                .isEmpty();
    }

    @Test
    void btreeIndexesOnArrayColumnsShouldReturnNothing() {
        assertThat(btreeIndexesOnArrayColumnsCheck.check())
                .isEmpty();
    }

    @Test
    void sequenceOverflowCheckShouldReturnNothing() {
        assertThat(sequenceOverflowCheck.check())
                .isEmpty();
    }

    @Test
    void getPrimaryKeysWithSerialTypesShouldReturnNothing() {
        assertThat(primaryKeysWithSerialTypesCheck.check())
                .isEmpty();
    }
}
