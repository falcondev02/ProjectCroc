package ru.croc.tasklast.project.daoimpl;

import ru.croc.tasklast.project.dao.UserTestDao;
import ru.croc.tasklast.project.database.DatabaseUtils;
import ru.croc.tasklast.project.model.Test;
import ru.croc.tasklast.project.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.croc.tasklast.project.database.DatabaseUtils.getConnection;

public class UserTestDaoImpl implements UserTestDao {
    // Singleton implementation, getConnection method, and closeConnection methods

    /**
     * Метод для добавления связи между пользователем и тестом в таблицу user_test.
     *
     * @param userId ID пользователя
     * @param testId ID теста
     */
    @Override
    public void assignTestToUser(int userId, int testId) {
        String sql = "INSERT INTO user_data_test (user_data_id, test_id) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, testId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для удаления связи между пользователем и тестом из таблицы user_test.
     *
     * @param userId ID пользователя
     * @param testId ID теста
     */
    @Override
    public void removeTestFromUser(int userId, int testId) {
        String sql = "DELETE FROM user_data_test WHERE user_data_id = ? AND test_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, testId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для получения списка тестов, назначенных пользователю.
     *
     * @param userId ID пользователя
     * @return список тестов, назначенных пользователю
     */
    @Override
    public List<Test> getTestsForUser(int userId) {
        List<Test> tests = new ArrayList<>();
        // Получаем ID тестов из таблицы user_test
        String userTestIdSql = "SELECT test_id FROM user_data_test WHERE user_data_id = ?";
        // Создаем SQL-запрос для получения теста по его ID
        String testSql = "SELECT * FROM test WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement userTestStmt = conn.prepareStatement(userTestIdSql);
             PreparedStatement testStmt = conn.prepareStatement(testSql)) {
            userTestStmt.setInt(1, userId);
            ResultSet userTestRs = userTestStmt.executeQuery();

            while (userTestRs.next()) {
                int testId = userTestRs.getInt("test_id");

                testStmt.setInt(1, testId);
                ResultSet testRs = testStmt.executeQuery();
                if (testRs.next()) {
                    String englishSentence = testRs.getString("english_sentence");
                    String russianSentence = testRs.getString("russian_sentence");

                    // Получаем список слов из базы данных
                    String[] wordsArray = (String[]) testRs.getArray("words_list").getArray();
                    List<String> wordsList = Arrays.asList(wordsArray);

                    Test test = new Test(testId, englishSentence, russianSentence, wordsList);
                    tests.add(test);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tests;
    }

    /**
     * Метод для получения списка пользователей, привязанных к определенному тесту.
     *
     * @param testId id теста
     * @return список пользователей, привязанных к тесту
     */
    @Override
    public List<User> getUsersForTest(int testId) {
        List<User> users = new ArrayList<>();
        // Получаем id пользователей из таблицы user_test
        String testUserIdSql = "SELECT user_data_id FROM user_data_test WHERE test_id = ?";
        // Создаем SQL-запрос для получения пользователей по их
        String userSql = "SELECT * FROM user_data WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement userTestStmt = conn.prepareStatement(testUserIdSql);
             PreparedStatement userStmt = conn.prepareStatement(userSql)) {
            userTestStmt.setInt(1, testId);
            ResultSet userTestRs = userTestStmt.executeQuery();

            while (userTestRs.next()) {
                int userId = userTestRs.getInt("user_data_id");

                userStmt.setInt(1, userId);
                ResultSet userRs = userStmt.executeQuery();
                if (userRs.next()) {
                    String userLogin = userRs.getString("user_login");
                    String userPassword = userRs.getString("user_password");
                    String userRole = userRs.getString("user_role");

                    User user = new User(userId, userLogin, userPassword, userRole);
                    users.add(user);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void deleteUserTestData(Test test) throws SQLException {
        String deleteQuery = "DELETE FROM user_data_test WHERE test_id = ?";
        Connection connection = DatabaseUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, test.getId());
            statement.executeUpdate();
        }
    }
}