package ru.croc.tasklast.project.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static ru.croc.tasklast.project.database.DatabaseUtils.getConnection;
// создание таблиц, прошу проверить их

public class CreateTable {
    private static String USER_DATA_TABLE =
            "CREATE TABLE IF NOT EXISTS user_data (" +
                "id            SERIAL PRIMARY KEY," +
                "user_login    TEXT NOT NULL," +
                "user_password TEXT NOT NULL," +
                "user_role     VARCHAR(5) NOT NULL DEFAULT 'USER'" +
            ")";
    private static String TEST_TABLE =
            "CREATE TABLE IF NOT EXISTS test (" +
                "id          SERIAL PRIMARY KEY," +
                "english_sentence TEXT NOT NULL," +
                "russian_sentence TEXT NOT NULL," +
                "words_list TEXT[]" +
            ")";

    private static  String TEST_RESULT_TABLE =
            "CREATE TABLE IF NOT EXISTS test_result (" +
                    "id           SERIAL PRIMARY KEY," +
                    "rating       INT NOT NULL," +
                    "test_id      INT NOT NULL REFERENCES test(id) ON DELETE CASCADE," +
                    "user_id      INT NOT NULL REFERENCES user_data(id) ON DELETE CASCADE," +
                    "user_answer TEXT NOT NULL" +
            ")";
/*
    private static String TEST_TEST_RESULT_TABLE =
            "CREATE TABLE IF NOT EXISTS test_test_result (" +

            ")";
*/


    public static void createUserDataTable() throws SQLException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(USER_DATA_TABLE);
        }
    }

    public static void createTestTable() throws SQLException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(TEST_TABLE);
        }
    }

/*    public static void createQuestionTable() throws SQLException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(QUESTION_TABLE);
        }
    }*/

    public static void createTestResultTable() throws SQLException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(TEST_RESULT_TABLE);
        }
    }


    public static void main(String[] args) throws SQLException {
        createUserDataTable();
        createTestTable();
        //createQuestionTable();
        createTestResultTable();
    }
}
