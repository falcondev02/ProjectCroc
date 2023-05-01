package ru.croc.tasklast.project.model;

import java.util.ArrayList;

public class User {
    private int id;
    private String userLogin;
    private String userPassword;
    private String role;
    private ArrayList<TestResult> testResultsList;

    public User() {
    }

    public User(int id, String userLogin, String userPassword, String role, ArrayList<TestResult> testResultsList) {
        this.id = id;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.role = role;
        this.testResultsList = testResultsList;
    }

    public User(int id, String userLogin, String userPassword, String role) {
        this.id = id;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.role = role;
        this.testResultsList = new ArrayList<>();
    }

    public User(String userLogin, String userPassword, String role) {
        this.id = 0;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.role = role;
        this.testResultsList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ArrayList<TestResult> getTestResultsList() {
        return testResultsList;
    }

    public void setTestResultsList(ArrayList<TestResult> testResultsList) {
        this.testResultsList = testResultsList;
    }


}
