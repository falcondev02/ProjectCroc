package ru.croc.tasklast.project.dao;

import ru.croc.tasklast.project.model.Test;
import ru.croc.tasklast.project.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserTestDao {
    void assignTestToUser(int userId, int testId);
    void removeTestFromUser(int userId, int testId);
    List<Test> getTestsForUser(int userId);
    List<User> getUsersForTest(int testId);
    void deleteUserTestData(Test test) throws SQLException;
}
