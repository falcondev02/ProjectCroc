package ru.croc.tasklast.project.database;

import java.sql.*;

public class DatabaseUtils {
    private static final String DATABASE_URL = "jdbc:h2:tcp://localhost:9092/D:/javaProjects/JavaCourse/db/example";
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
    }

    public static void truncateAllTables() throws SQLException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM test_result");
            statement.executeUpdate("DELETE FROM user_data_test");
            statement.executeUpdate("DELETE FROM test");
            statement.executeUpdate("DELETE FROM user_data");

        }
    }
/*
    public static void dbClean() {
        try (Connection con = DatabaseUtils.getConnection();
             Statement stmt = con.createStatement()) {

            String[] tableNames = {"test_result", "test", "user_data", "question"}; // список таблиц для удаления

            for (String tableName : tableNames) {
                stmt.executeUpdate("DROP TABLE IF EXISTS " + tableName + " CASCADE;");
                System.out.println("Table " + tableName + " dropped successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

/*    public static void main(String[] args) {
        dbClean();
    }*/
}
