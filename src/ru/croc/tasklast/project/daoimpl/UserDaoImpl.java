package ru.croc.tasklast.project.daoimpl;

import ru.croc.tasklast.project.dao.UserDao;
import ru.croc.tasklast.project.database.DatabaseUtils;
import ru.croc.tasklast.project.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public void createUser(User user) throws SQLException {
        String insertQuery = "INSERT INTO user_data (user_login, user_password, user_role) VALUES (?, ?, ?)";
        Connection connection = DatabaseUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUserLogin());
            statement.setString(2, user.getUserPassword());
            statement.setString(3, user.getRole());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public User getUserById(int id) throws SQLException {
        String query = "SELECT * FROM user_data WHERE id = ?";
        Connection connection = DatabaseUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String userLogin = resultSet.getString("user_login");
                    String userPassword = resultSet.getString("user_password");
                    String role = resultSet.getString("role");
                    return new User(id, userLogin, userPassword, role);
                }
                return null;
            }
        }
    }
    @Override
    public int getMaxUserId() throws SQLException {
        String query = "SELECT MAX(id) FROM user_data";
        Connection connection = DatabaseUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        }
    }

    @Override
    public User getUserByLogin(String login) throws SQLException {
        String query = "SELECT * FROM user_data WHERE user_login = ?";
        Connection connection = DatabaseUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String userPassword = resultSet.getString("user_password");
                    String role = resultSet.getString("role");
                    return new User(id, login, userPassword, role);
                }
                return null;
            }
        }
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        String query = "SELECT * FROM user_data";
        Connection connection = DatabaseUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            List<User> userList = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String userLogin = resultSet.getString("user_login");
                String userPassword = resultSet.getString("user_password");
                String role = resultSet.getString("role");
                userList.add(new User(id, userLogin, userPassword, role));
            }
            return userList;
        }
    }


    @Override
    public void updateUser(User user) throws SQLException {
        String query = "UPDATE user_data SET user_login = ?, user_password = ?, role = ? WHERE id = " + user.getId();
        Connection connection = DatabaseUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUserLogin());
            statement.setString(2, user.getUserPassword());
            statement.setString(3, user.getRole().toString());
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteUser(User user) throws SQLException {
        String query = "DELETE FROM user_data WHERE id =" + user.getId();
        Connection connection = DatabaseUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        }
    }
}
