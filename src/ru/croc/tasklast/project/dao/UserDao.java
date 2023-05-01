package ru.croc.tasklast.project.dao;

import ru.croc.tasklast.project.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    void createUser(User user) throws SQLException;
    User getUserById(int id) throws SQLException;
    User getUserByLogin(String login) throws SQLException;
    List<User> getAllUsers() throws SQLException;
    void updateUser(User user) throws SQLException;
    void deleteUser(User user) throws SQLException;
    int getMaxUserId() throws SQLException;
}
