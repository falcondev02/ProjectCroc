package ru.croc.tasklast.project.daoimpl;

import ru.croc.tasklast.project.dao.TestResultDao;
import ru.croc.tasklast.project.database.DatabaseUtils;
import ru.croc.tasklast.project.model.Test;
import ru.croc.tasklast.project.model.TestResult;
import ru.croc.tasklast.project.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TestResultDaoImpl implements TestResultDao {
    @Override
    public void createTestResult(TestResult testResult, User user, Test test) throws SQLException {
        String insertQuery = "INSERT INTO test_result (rating, test_id, user_id, user_answer) VALUES (?,?,?,?)";
        Connection connection = DatabaseUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, testResult.getRating());
            statement.setInt(2, test.getId());
            statement.setInt(3, user.getId());
            statement.setString(4, testResult.getUserAnswer());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    testResult.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating testResult failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public TestResult getTestResultById(int id) throws SQLException {
        Connection connection = DatabaseUtils.getConnection();
        String selectQuery = "SELECT * FROM test_result WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    TestResult testResult = new TestResult();
                    testResult.setId(resultSet.getInt("id"));
                    testResult.setRating(resultSet.getInt("rating"));

                    Test test = new Test();
                    test.setId(resultSet.getInt("test_id"));

                    User user = new User();
                    user.setId(resultSet.getInt("user_id"));

                    testResult.setTest(test);
                    testResult.setUser(user);
                    testResult.setUserAnswer(resultSet.getString("user_answer"));

                    return testResult;
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public List<TestResult> getTestResultsByUserId(int userId) throws SQLException {
        Connection connection = DatabaseUtils.getConnection();
        String selectQuery = "SELECT * FROM test_result WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<TestResult> testResults = new ArrayList<>();
                while (resultSet.next()) {
                    TestResult testResult = new TestResult();
                    testResult.setId(resultSet.getInt("id"));
                    testResult.setRating(resultSet.getInt("rating"));

                    Test test = new Test();
                    test.setId(resultSet.getInt("test_id"));

                    User user = new User();
                    user.setId(resultSet.getInt("user_id"));

                    testResult.setTest(test);
                    testResult.setUser(user);
                    testResult.setUserAnswer(resultSet.getString("user_answer"));

                    testResults.add(testResult);
                }

                return testResults;
            }
        }
    }



    @Override
    public List<TestResult> getTestResultsByTestId(int testId) throws SQLException {
        Connection connection = DatabaseUtils.getConnection();
        String selectQuery = "SELECT * FROM test_result WHERE test_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setInt(1, testId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<TestResult> testResults = new ArrayList<>();
                while (resultSet.next()) {
                    TestResult testResult = new TestResult();
                    testResult.setId(resultSet.getInt("id"));
                    testResult.setRating(resultSet.getInt("rating"));

                    Test test = new Test();
                    test.setId(resultSet.getInt("test_id"));

                    User user = new User();
                    user.setId(resultSet.getInt("user_id"));

                    testResult.setTest(test);
                    testResult.setUser(user);
                    testResult.setUserAnswer(resultSet.getString("user_answer"));

                    testResults.add(testResult);
                }

                return testResults;
            }
        }
    }

    @Override
    public void updateTestResult(TestResult testResult) throws SQLException {
        Connection connection = DatabaseUtils.getConnection();
        String updateQuery = "UPDATE test_result SET rating = ?, user_answer = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setInt(1, testResult.getRating());
            statement.setString(2, testResult.getUserAnswer());
            statement.setInt(3, testResult.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteTestResult(TestResult testResult) throws SQLException {
        Connection connection = DatabaseUtils.getConnection();
        String deleteQuery = "DELETE FROM test_result WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, testResult.getId());
            statement.executeUpdate();
        }
    }

    public static TestResult runTest(Test test, User user, String userAnswer) {
        // Implement the necessary logic to compare the userAnswer with the correct answer

        int correctWords = 0;
        int totalWords = test.getWordsList().size();

        String[] correctAnswerWords = test.getEnglishSentence().split(" ");
        String[] userAnswerWords = userAnswer.split(" ");

        for (int i = 0; i < correctAnswerWords.length; i++) {
            if (userAnswerWords.length > i && userAnswerWords[i].equals(correctAnswerWords[i])) {
                correctWords++;
            }
        }

        int rating = (int) (((double) correctWords / totalWords) * 100);

        TestResult testResult = new TestResult(0, rating, test, user, userAnswer);
        return testResult;
    }

}
