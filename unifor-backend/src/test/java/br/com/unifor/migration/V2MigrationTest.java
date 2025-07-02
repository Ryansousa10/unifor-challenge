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
class V2MigrationTest {

    @Inject
    AgroalDataSource dataSource;

    private static final String SCHEMA = "PUBLIC";

    @Test
    void testCourseTableStructure() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             ResultSet columns = conn.getMetaData().getColumns(null, SCHEMA, "course", null)) {

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
            Assertions.assertTrue(columnNames.contains("code"), "Coluna 'code' não encontrada");
            Assertions.assertTrue(columnNames.contains("name"), "Coluna 'name' não encontrada");
            Assertions.assertTrue(columnNames.contains("description"), "Coluna 'description' não encontrada");

            // Verifica tipos das colunas
            int idIndex = columnNames.indexOf("id");
            int codeIndex = columnNames.indexOf("code");
            int nameIndex = columnNames.indexOf("name");
            int descriptionIndex = columnNames.indexOf("description");

            Assertions.assertTrue(columnTypes.get(idIndex).contains("uuid"), "Tipo da coluna 'id' incorreto");
            Assertions.assertTrue(columnTypes.get(codeIndex).contains("varchar") || columnTypes.get(codeIndex).contains("character varying"), "Tipo da coluna 'code' incorreto");
            Assertions.assertTrue(columnTypes.get(nameIndex).contains("varchar") || columnTypes.get(nameIndex).contains("character varying"), "Tipo da coluna 'name' incorreto");
            Assertions.assertTrue(columnTypes.get(descriptionIndex).contains("text") ||
                                columnTypes.get(descriptionIndex).contains("clob") ||
                                columnTypes.get(descriptionIndex).contains("character large object"),
                                "Tipo da coluna 'description' incorreto");
        }
    }

    @Test
    void testSemesterTableStructure() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             ResultSet columns = conn.getMetaData().getColumns(null, SCHEMA, "semester", null)) {

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
            Assertions.assertTrue(columnNames.contains("name"), "Coluna 'name' não encontrada");
            Assertions.assertTrue(columnNames.contains("start_date"), "Coluna 'start_date' não encontrada");
            Assertions.assertTrue(columnNames.contains("end_date"), "Coluna 'end_date' não encontrada");

            // Verifica tipos das colunas
            int idIndex = columnNames.indexOf("id");
            int nameIndex = columnNames.indexOf("name");
            int startDateIndex = columnNames.indexOf("start_date");
            int endDateIndex = columnNames.indexOf("end_date");

            Assertions.assertTrue(columnTypes.get(idIndex).contains("uuid"), "Tipo da coluna 'id' incorreto");
            Assertions.assertTrue(columnTypes.get(nameIndex).contains("varchar") || columnTypes.get(nameIndex).contains("character varying"), "Tipo da coluna 'name' incorreto");
            Assertions.assertTrue(columnTypes.get(startDateIndex).contains("date"), "Tipo da coluna 'start_date' incorreto");
            Assertions.assertTrue(columnTypes.get(endDateIndex).contains("date"), "Tipo da coluna 'end_date' incorreto");
        }
    }

    @Test
    void testDisciplineTableStructure() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             ResultSet columns = conn.getMetaData().getColumns(null, SCHEMA, "discipline", null)) {

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
            Assertions.assertTrue(columnNames.contains("code"), "Coluna 'code' não encontrada");
            Assertions.assertTrue(columnNames.contains("name"), "Coluna 'name' não encontrada");
            Assertions.assertTrue(columnNames.contains("credits"), "Coluna 'credits' não encontrada");
            Assertions.assertTrue(columnNames.contains("description"), "Coluna 'description' não encontrada");

            // Verifica tipos das colunas
            int idIndex = columnNames.indexOf("id");
            int codeIndex = columnNames.indexOf("code");
            int nameIndex = columnNames.indexOf("name");
            int creditsIndex = columnNames.indexOf("credits");
            int descriptionIndex = columnNames.indexOf("description");

            Assertions.assertTrue(columnTypes.get(idIndex).contains("uuid"), "Tipo da coluna 'id' incorreto");
            Assertions.assertTrue(columnTypes.get(codeIndex).contains("varchar") || columnTypes.get(codeIndex).contains("character varying"), "Tipo da coluna 'code' incorreto");
            Assertions.assertTrue(columnTypes.get(nameIndex).contains("varchar") || columnTypes.get(nameIndex).contains("character varying"), "Tipo da coluna 'name' incorreto");
            Assertions.assertTrue(columnTypes.get(creditsIndex).contains("integer") || columnTypes.get(creditsIndex).contains("int"), "Tipo da coluna 'credits' incorreto");
            Assertions.assertTrue(columnTypes.get(descriptionIndex).contains("text") ||
                                columnTypes.get(descriptionIndex).contains("clob") ||
                                columnTypes.get(descriptionIndex).contains("character large object"),
                                "Tipo da coluna 'description' incorreto");
        }
    }

    @Test
    void testUniqueConstraints() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            // Verifica unique constraint em course.code
            try (ResultSet courseConstraints = conn.getMetaData().getIndexInfo(null, SCHEMA, "course", true, false)) {
                List<String> uniqueColumns = new ArrayList<>();
                while (courseConstraints.next()) {
                    String columnName = courseConstraints.getString("COLUMN_NAME");
                    if (columnName != null) {
                        uniqueColumns.add(columnName.toLowerCase());
                    }
                }
                Assertions.assertTrue(uniqueColumns.contains("code"), "course.code deve ter constraint UNIQUE");
            }

            // Verifica unique constraint em semester.name
            try (ResultSet semesterConstraints = conn.getMetaData().getIndexInfo(null, SCHEMA, "semester", true, false)) {
                List<String> uniqueColumns = new ArrayList<>();
                while (semesterConstraints.next()) {
                    String columnName = semesterConstraints.getString("COLUMN_NAME");
                    if (columnName != null) {
                        uniqueColumns.add(columnName.toLowerCase());
                    }
                }
                Assertions.assertTrue(uniqueColumns.contains("name"), "semester.name deve ter constraint UNIQUE");
            }

            // Verifica unique constraint em discipline.code
            try (ResultSet disciplineConstraints = conn.getMetaData().getIndexInfo(null, SCHEMA, "discipline", true, false)) {
                List<String> uniqueColumns = new ArrayList<>();
                while (disciplineConstraints.next()) {
                    String columnName = disciplineConstraints.getString("COLUMN_NAME");
                    if (columnName != null) {
                        uniqueColumns.add(columnName.toLowerCase());
                    }
                }
                Assertions.assertTrue(uniqueColumns.contains("code"), "discipline.code deve ter constraint UNIQUE");
            }
        }
    }
}
