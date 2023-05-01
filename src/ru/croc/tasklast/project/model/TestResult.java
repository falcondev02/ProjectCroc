package ru.croc.tasklast.project.model;

public class TestResult {
    private int id;
    private int rating;
    private Test test;
    private User user;
    private String userAnswer;

    public TestResult() {
    }

    public TestResult(int id, int rating, Test test, User user, String userAnswer) {
        this.id = id;
        this.rating = rating;
        this.test = test;
        this.user = user;
        this.userAnswer = userAnswer;
    }

    public TestResult(int rating, Test test, User user, String userAnswer) {
        this.rating = rating;
        this.test = test;
        this.user = user;
        this.userAnswer = userAnswer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
