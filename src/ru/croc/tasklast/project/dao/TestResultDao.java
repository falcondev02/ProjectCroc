package ru.croc.tasklast.project.dao;

import ru.croc.tasklast.project.model.Test;
import ru.croc.tasklast.project.model.TestResult;
import ru.croc.tasklast.project.model.User;

import java.sql.SQLException;
import java.util.List;

public interface TestResultDao {
    void createTestResult(TestResult testResult, User user, Test test) throws SQLException;
    TestResult getTestResultById(int id) throws SQLException;
    List<TestResult> getTestResultsByUserId(int userId) throws SQLException;
    List<TestResult> getTestResultsByTestId(int testId) throws SQLException;
    void updateTestResult(TestResult testResult) throws SQLException;
    void deleteTestResult(TestResult testResult) throws SQLException;
}
