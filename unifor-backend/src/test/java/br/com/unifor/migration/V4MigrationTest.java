package br.com.unifor.migration;

import io.quarkus.test.junit.QuarkusTest;
import io.agroal.api.AgroalDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@QuarkusTest
class V4MigrationTest {

    @Inject
    AgroalDataSource dataSource;

    private static final String SCHEMA = "PUBLIC";

    @Test
    void testEnrollmentTableStructure() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             ResultSet columns = conn.getMetaData().getColumns(null, SCHEMA, "enrollment", null)) {

            List<String> columnNames = new ArrayList<>();
            List<String> columnTypes = new ArrayList<>();

            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME").toLowerCase();
                String columnType = columns.getString("TYPE_NAME").toLowerCase();
                columnNames.add(columnName);
                columnTypes.add(columnType);
                System.out.println("Coluna: " + columnName + ", Tipo: " + columnType);
            }

            // Verifica existência das colunas
            Assertions.assertTrue(columnNames.contains("id"), "Coluna 'id' não encontrada");
            Assertions.assertTrue(columnNames.contains("student_id"), "Coluna 'student_id' não encontrada");
            Assertions.assertTrue(columnNames.contains("curriculum_id"), "Coluna 'curriculum_id' não encontrada");
            Assertions.assertTrue(columnNames.contains("enrolled_at"), "Coluna 'enrolled_at' não encontrada");

            // Verifica tipos das colunas
            int idIndex = columnNames.indexOf("id");
            int studentIdIndex = columnNames.indexOf("student_id");
            int curriculumIdIndex = columnNames.indexOf("curriculum_id");
            int enrolledAtIndex = columnNames.indexOf("enrolled_at");

            Assertions.assertTrue(columnTypes.get(idIndex).contains("uuid"), "Tipo da coluna 'id' incorreto");
            Assertions.assertTrue(columnTypes.get(studentIdIndex).contains("uuid"), "Tipo da coluna 'student_id' incorreto");
            Assertions.assertTrue(columnTypes.get(curriculumIdIndex).contains("uuid"), "Tipo da coluna 'curriculum_id' incorreto");
            Assertions.assertTrue(columnTypes.get(enrolledAtIndex).contains("timestamp"), "Tipo da coluna 'enrolled_at' incorreto");
        }
    }

    @Test
    void testForeignKeyConstraints() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             ResultSet enrollmentFKs = conn.getMetaData().getImportedKeys(null, SCHEMA, "enrollment")) {

            List<String> fkColumns = new ArrayList<>();
            List<String> pkTables = new ArrayList<>();

            while (enrollmentFKs.next()) {
                String fkColumn = enrollmentFKs.getString("FKCOLUMN_NAME");
                String pkTable = enrollmentFKs.getString("PKTABLE_NAME");
                if (fkColumn != null && pkTable != null) {
                    fkColumns.add(fkColumn.toLowerCase());
                    pkTables.add(pkTable.toLowerCase());
                }
                System.out.println("FK: " + fkColumn + " -> " + pkTable);
            }

            // Verifica FKs do enrollment
            Assertions.assertTrue(fkColumns.contains("student_id"), "FK student_id não encontrada");
            Assertions.assertTrue(fkColumns.contains("curriculum_id"), "FK curriculum_id não encontrada");
            Assertions.assertTrue(pkTables.contains("users"), "FK para tabela users não encontrada");
            Assertions.assertTrue(pkTables.contains("curriculum"), "FK para tabela curriculum não encontrada");
        }
    }
}
