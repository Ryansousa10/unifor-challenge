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
class V3MigrationTest {

    @Inject
    AgroalDataSource dataSource;

    private static final String SCHEMA = "PUBLIC";

    @Test
    void testCurriculumTableStructure() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             ResultSet columns = conn.getMetaData().getColumns(null, SCHEMA, "curriculum", null)) {

            List<String> columnNames = new ArrayList<>();
            List<String> columnTypes = new ArrayList<>();

            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME").toLowerCase();
                String columnType = columns.getString("TYPE_NAME").toLowerCase();
                columnNames.add(columnName);
                columnTypes.add(columnType);
                System.out.println("Coluna: " + columnName + ", Tipo: " + columnType);
            }

            Assertions.assertTrue(columnNames.contains("id"), "Coluna 'id' não encontrada");
            Assertions.assertTrue(columnNames.contains("course_id"), "Coluna 'course_id' não encontrada");
            Assertions.assertTrue(columnNames.contains("semester_id"), "Coluna 'semester_id' não encontrada");

            // Verifica tipos das colunas
            int idIndex = columnNames.indexOf("id");
            int courseIdIndex = columnNames.indexOf("course_id");
            int semesterIdIndex = columnNames.indexOf("semester_id");

            Assertions.assertTrue(columnTypes.get(idIndex).contains("uuid"), "Tipo da coluna 'id' incorreto");
            Assertions.assertTrue(columnTypes.get(courseIdIndex).contains("uuid"), "Tipo da coluna 'course_id' incorreto");
            Assertions.assertTrue(columnTypes.get(semesterIdIndex).contains("uuid"), "Tipo da coluna 'semester_id' incorreto");
        }
    }

    @Test
    void testCurricDiscTableStructure() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             ResultSet columns = conn.getMetaData().getColumns(null, SCHEMA, "curric_disc", null)) {

            List<String> columnNames = new ArrayList<>();
            List<String> columnTypes = new ArrayList<>();

            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME").toLowerCase();
                String columnType = columns.getString("TYPE_NAME").toLowerCase();
                columnNames.add(columnName);
                columnTypes.add(columnType);
                System.out.println("Coluna: " + columnName + ", Tipo: " + columnType);
            }

            Assertions.assertTrue(columnNames.contains("curriculum_id"), "Coluna 'curriculum_id' não encontrada");
            Assertions.assertTrue(columnNames.contains("discipline_id"), "Coluna 'discipline_id' não encontrada");
            Assertions.assertTrue(columnNames.contains("ordering"), "Coluna 'ordering' não encontrada");

            // Verifica tipos das colunas
            int curriculumIdIndex = columnNames.indexOf("curriculum_id");
            int disciplineIdIndex = columnNames.indexOf("discipline_id");
            int orderingIndex = columnNames.indexOf("ordering");

            Assertions.assertTrue(columnTypes.get(curriculumIdIndex).contains("uuid"), "Tipo da coluna 'curriculum_id' incorreto");
            Assertions.assertTrue(columnTypes.get(disciplineIdIndex).contains("uuid"), "Tipo da coluna 'discipline_id' incorreto");
            Assertions.assertTrue(columnTypes.get(orderingIndex).contains("integer") || columnTypes.get(orderingIndex).contains("int"), "Tipo da coluna 'ordering' incorreto");
        }
    }

    @Test
    void testForeignKeyConstraints() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             ResultSet curriculumFKs = conn.getMetaData().getImportedKeys(null, SCHEMA, "curriculum")) {

            List<String> fkColumns = new ArrayList<>();
            List<String> pkTables = new ArrayList<>();

            while (curriculumFKs.next()) {
                String fkColumn = curriculumFKs.getString("FKCOLUMN_NAME");
                String pkTable = curriculumFKs.getString("PKTABLE_NAME");
                if (fkColumn != null && pkTable != null) {
                    fkColumns.add(fkColumn.toLowerCase());
                    pkTables.add(pkTable.toLowerCase());
                }
                System.out.println("FK: " + fkColumn + " -> " + pkTable);
            }

            // Verifica FKs do curriculum
            Assertions.assertTrue(fkColumns.contains("course_id"), "FK course_id não encontrada");
            Assertions.assertTrue(fkColumns.contains("semester_id"), "FK semester_id não encontrada");
            Assertions.assertTrue(pkTables.contains("course"), "FK para tabela course não encontrada");
            Assertions.assertTrue(pkTables.contains("semester"), "FK para tabela semester não encontrada");
        }

        try (Connection conn = dataSource.getConnection();
             ResultSet curricDiscFKs = conn.getMetaData().getImportedKeys(null, SCHEMA, "curric_disc")) {

            List<String> fkColumns = new ArrayList<>();
            List<String> pkTables = new ArrayList<>();

            while (curricDiscFKs.next()) {
                String fkColumn = curricDiscFKs.getString("FKCOLUMN_NAME");
                String pkTable = curricDiscFKs.getString("PKTABLE_NAME");
                if (fkColumn != null && pkTable != null) {
                    fkColumns.add(fkColumn.toLowerCase());
                    pkTables.add(pkTable.toLowerCase());
                }
                System.out.println("FK: " + fkColumn + " -> " + pkTable);
            }

            // Verifica FKs do curric_disc
            Assertions.assertTrue(fkColumns.contains("curriculum_id"), "FK curriculum_id não encontrada");
            Assertions.assertTrue(fkColumns.contains("discipline_id"), "FK discipline_id não encontrada");
            Assertions.assertTrue(pkTables.contains("curriculum"), "FK para tabela curriculum não encontrada");
            Assertions.assertTrue(pkTables.contains("discipline"), "FK para tabela discipline não encontrada");
        }
    }

    @Test
    void testCurricDiscUniqueConstraint() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             ResultSet curricDiscConstraints = conn.getMetaData().getIndexInfo(null, SCHEMA, "curric_disc", true, false)) {

            List<String> uniqueColumns = new ArrayList<>();
            while (curricDiscConstraints.next()) {
                String columnName = curricDiscConstraints.getString("COLUMN_NAME");
                if (columnName != null) {
                    uniqueColumns.add(columnName.toLowerCase());
                    System.out.println("Coluna única: " + columnName);
                }
            }

            // Para uma constraint unique composta, ambas as colunas devem estar presentes
            Assertions.assertTrue(uniqueColumns.contains("curriculum_id") && uniqueColumns.contains("discipline_id"),
                "curric_disc deve ter constraint UNIQUE em (curriculum_id, discipline_id)");
        }
    }
}
