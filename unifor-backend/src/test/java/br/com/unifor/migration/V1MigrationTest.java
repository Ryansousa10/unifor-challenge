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
class V1MigrationTest {

    @Inject
    AgroalDataSource dataSource;

    private static final String SCHEMA = "PUBLIC";

    @Test
    void testRoleTableStructure() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             ResultSet columns = conn.getMetaData().getColumns(null, SCHEMA, "role", null)) {

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

            // Verifica tipos das colunas
            int idIndex = columnNames.indexOf("id");
            int nameIndex = columnNames.indexOf("name");

            Assertions.assertTrue(columnTypes.get(idIndex).contains("char") || columnTypes.get(idIndex).contains("uuid"), "Tipo da coluna 'id' incorreto");
            Assertions.assertTrue(columnTypes.get(nameIndex).contains("varchar") || columnTypes.get(nameIndex).contains("character varying"), "Tipo da coluna 'name' incorreto");
        }
    }

    @Test
    void testUserTableStructure() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             ResultSet columns = conn.getMetaData().getColumns(null, SCHEMA, "users", null)) {

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
            Assertions.assertTrue(columnNames.contains("username"), "Coluna 'username' não encontrada");
            Assertions.assertTrue(columnNames.contains("password"), "Coluna 'password' não encontrada");
            Assertions.assertTrue(columnNames.contains("first_name"), "Coluna 'first_name' não encontrada");
            Assertions.assertTrue(columnNames.contains("last_name"), "Coluna 'last_name' não encontrada");
            Assertions.assertTrue(columnNames.contains("email"), "Coluna 'email' não encontrada");
            Assertions.assertTrue(columnNames.contains("created_at"), "Coluna 'created_at' não encontrada");

            // Verifica tipos das colunas
            int idIndex = columnNames.indexOf("id");
            int usernameIndex = columnNames.indexOf("username");
            int passwordIndex = columnNames.indexOf("password");
            int firstNameIndex = columnNames.indexOf("first_name");
            int lastNameIndex = columnNames.indexOf("last_name");
            int emailIndex = columnNames.indexOf("email");
            int createdAtIndex = columnNames.indexOf("created_at");

            Assertions.assertTrue(columnTypes.get(idIndex).contains("char") || columnTypes.get(idIndex).contains("uuid"), "Tipo da coluna 'id' incorreto");
            Assertions.assertTrue(columnTypes.get(usernameIndex).contains("varchar") || columnTypes.get(usernameIndex).contains("character varying"), "Tipo da coluna 'username' incorreto");
            Assertions.assertTrue(columnTypes.get(passwordIndex).contains("varchar") || columnTypes.get(passwordIndex).contains("character varying"), "Tipo da coluna 'password' incorreto");
            Assertions.assertTrue(columnTypes.get(firstNameIndex).contains("varchar") || columnTypes.get(firstNameIndex).contains("character varying"), "Tipo da coluna 'first_name' incorreto");
            Assertions.assertTrue(columnTypes.get(lastNameIndex).contains("varchar") || columnTypes.get(lastNameIndex).contains("character varying"), "Tipo da coluna 'last_name' incorreto");
            Assertions.assertTrue(columnTypes.get(emailIndex).contains("varchar") || columnTypes.get(emailIndex).contains("character varying"), "Tipo da coluna 'email' incorreto");
            Assertions.assertTrue(columnTypes.get(createdAtIndex).contains("timestamp"), "Tipo da coluna 'created_at' incorreto");
        }
    }

    @Test
    void testUserRoleTableStructure() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             ResultSet columns = conn.getMetaData().getColumns(null, SCHEMA, "user_role", null)) {

            List<String> columnNames = new ArrayList<>();
            List<String> columnTypes = new ArrayList<>();

            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME").toLowerCase();
                String columnType = columns.getString("TYPE_NAME").toLowerCase();
                columnNames.add(columnName);
                columnTypes.add(columnType);
                System.out.println("Coluna: " + columnName + ", Tipo: " + columnType);
            }

            Assertions.assertTrue(columnNames.contains("user_id"), "Coluna 'user_id' não encontrada");
            Assertions.assertTrue(columnNames.contains("role_id"), "Coluna 'role_id' não encontrada");

            // Verifica tipos das colunas
            int userIdIndex = columnNames.indexOf("user_id");
            int roleIdIndex = columnNames.indexOf("role_id");

            Assertions.assertTrue(columnTypes.get(userIdIndex).contains("char") || columnTypes.get(userIdIndex).contains("uuid"), "Tipo da coluna 'user_id' incorreto");
            Assertions.assertTrue(columnTypes.get(roleIdIndex).contains("char") || columnTypes.get(roleIdIndex).contains("uuid"), "Tipo da coluna 'role_id' incorreto");
        }
    }

    @Test
    void testDefaultRolesInserted() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             ResultSet roles = conn.createStatement().executeQuery("SELECT name FROM role ORDER BY name")) {

            List<String> roleNames = new ArrayList<>();
            while (roles.next()) {
                roleNames.add(roles.getString("name").toUpperCase());
            }

            Assertions.assertEquals(4, roleNames.size(), "Deve ter 4 roles padrão");
            Assertions.assertTrue(roleNames.contains("ADMIN"), "Role ADMIN não encontrada");
            Assertions.assertTrue(roleNames.contains("ALUNO"), "Role ALUNO não encontrada");
            Assertions.assertTrue(roleNames.contains("COORDENADOR"), "Role COORDENADOR não encontrada");
            Assertions.assertTrue(roleNames.contains("PROFESSOR"), "Role PROFESSOR não encontrada");
        }
    }

    @Test
    void testUniqueConstraints() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            // Verifica unique constraint em role.name
            try (ResultSet roleConstraints = conn.getMetaData().getIndexInfo(null, SCHEMA, "role", true, false)) {
                List<String> uniqueColumns = new ArrayList<>();
                while (roleConstraints.next()) {
                    String columnName = roleConstraints.getString("COLUMN_NAME");
                    if (columnName != null) {
                        uniqueColumns.add(columnName.toLowerCase());
                    }
                }
                Assertions.assertTrue(uniqueColumns.contains("name"), "role.name deve ter constraint UNIQUE");
            }

            // Verifica unique constraints em users
            try (ResultSet userConstraints = conn.getMetaData().getIndexInfo(null, SCHEMA, "users", true, false)) {
                List<String> uniqueColumns = new ArrayList<>();
                while (userConstraints.next()) {
                    String columnName = userConstraints.getString("COLUMN_NAME");
                    if (columnName != null) {
                        uniqueColumns.add(columnName.toLowerCase());
                    }
                }
                Assertions.assertTrue(uniqueColumns.contains("username"), "users.username deve ter constraint UNIQUE");
                Assertions.assertTrue(uniqueColumns.contains("email"), "users.email deve ter constraint UNIQUE");
            }
        }
    }
}
