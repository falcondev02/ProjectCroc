package ru.croc.tasklast.project;

import ru.croc.tasklast.project.dao.TestDao;
import ru.croc.tasklast.project.dao.TestResultDao;
import ru.croc.tasklast.project.dao.UserDao;
import ru.croc.tasklast.project.dao.UserTestDao;
import ru.croc.tasklast.project.daoimpl.TestDaoImpl;
import ru.croc.tasklast.project.daoimpl.TestResultDaoImpl;
import ru.croc.tasklast.project.daoimpl.UserDaoImpl;
import ru.croc.tasklast.project.daoimpl.UserTestDaoImpl;
import ru.croc.tasklast.project.database.DatabaseUtils;
import ru.croc.tasklast.project.model.Test;
import ru.croc.tasklast.project.model.TestResult;
import ru.croc.tasklast.project.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final TestDao testDao = new TestDaoImpl();
    private static final TestResultDao testResultDao = new TestResultDaoImpl();
    private static final UserDao userDao = new UserDaoImpl();
    private static final UserTestDao userTestDao = new UserTestDaoImpl();
    public static void main(String[] args) throws SQLException, IOException {
        DatabaseUtils.truncateAllTables();
        Scanner scanner = new Scanner(System.in);
        String operation = "";
        User user;
        System.out.println("Введите login and password");
        String login = scanner.next();
        String password = scanner.next();


        if (login.equals("admin") && password.equals("admin")) {
            user = new User(login, password, "ADMIN");
            userDao.createUser(user);
        } else {
            user  = new User(login, password, "USER");
            userDao.createUser(user);
        }

        if (user != null) {
            System.out.println("User logged in successfully");
        } else {
            System.out.println("Invalid login or password");
        }

        while (!operation.equalsIgnoreCase("9")) {

            System.out.println("Enter an option:");
            System.out.println("1: Create Test");
            System.out.println("2: Edit Test");
            System.out.println("3: Delete Test");
            System.out.println("4: Start Test");
            System.out.println("5: View Test Results");
            System.out.println("6: Import Tests from CSV");
            System.out.println("7: Export Tests to XML");
            System.out.println("9: Exit");

            operation = scanner.next();

            switch (operation) {
                case "1":
                    System.out.println("Количество английских слов в предложении");
                    int countEnglishSentence = scanner.nextInt();
                    String englishSentence = "";
                    for(int i = 0; i < countEnglishSentence; i ++) {
                        englishSentence += scanner.next();
                    }

                    System.out.println("Количество русских слов в предложении");
                    int countRussianSentence = scanner.nextInt();

                    String russianSentence = "";
                    for(int i = 0; i < countRussianSentence; i ++) {
                        russianSentence += scanner.next();
                    }

                    String[] words = englishSentence.split(" ");
                    Collections.shuffle(Arrays.asList(words));
                    Test newTest = testDao.createTest(new Test(testDao.getMaxTestId() +1 ,englishSentence, russianSentence, List.of(words)));
                    if (newTest != null) {
                        System.out.println("Test created successfully");
                    } else {
                        System.out.println("Test creation failed");
                    }
                    break;
                case "2":
                    System.out.println("Enter test ID, new English sentence, new Russian sentence, and new comma-separated words");
                    System.out.println("last max id: " + testDao.getMaxTestId());
                    int testId = scanner.nextInt();
                    System.out.println("Количество английских слов в предложении");
                    int countEnglishSentenceUpdate = scanner.nextInt();
                    String englishSentenceUpdate = "";
                    for(int i = 0; i < countEnglishSentenceUpdate; i ++) {
                        englishSentenceUpdate += scanner.next();
                    }

                    System.out.println("Количество русских слов в предложении");
                    int countRussianSentenceUpdate = scanner.nextInt();

                    String russianSentenceUpdate = "";
                    for(int i = 0; i < countRussianSentenceUpdate; i ++) {
                        russianSentenceUpdate += scanner.next();
                    }

                    String[] newWordsList = englishSentenceUpdate.split(" ");
                    Collections.shuffle(Arrays.asList(newWordsList));
                    boolean testUpdated = testDao.updateTest(new Test(testId, englishSentenceUpdate, russianSentenceUpdate, List.of(newWordsList)));
                    if (testUpdated) {
                        System.out.println("Test updated successfully");
                    } else {
                        System.out.println("Test update failed");
                    }
                    break;
                case "3":
                    System.out.println("Enter test ID");
                    int deleteTestId = scanner.nextInt();
                    Test deletedTest = testDao.getTestById(deleteTestId);
                    userTestDao.deleteUserTestData(deletedTest);
                    boolean testDeleted = testDao.deleteTest(deletedTest);
                    if (testDeleted) {
                        System.out.println("Test deleted successfully");
                    } else {
                        System.out.println("Test deletion failed");
                    }
                    break;
                case "4":
                    System.out.println("Starting test...");
                    List<Test> tests = testDao.getAllTests();

                    for (Test test : tests) {
                        userTestDao.assignTestToUser(user.getId(), test.getId());
                        System.out.println(test.getRussianSentence() + " and " + test.getWordsList());
                        String userAnswer = "";
                        System.out.println("Напишите английское предложение");
                        for(int j = 0; j < test.getWordsList().size() -1; j ++) {
                            userAnswer += scanner.next() + " ";
                        }
                        userAnswer += scanner.next();
                        TestResult testResult = TestResultDaoImpl.runTest(test, user, userAnswer);
                        testResultDao.createTestResult(testResult, user, test);
                    }

                    break;
                case "5":
                    System.out.println("Test results:");
                    List<TestResult> testResults = testResultDao.getTestResultsByUserId(user.getId());
                    for (TestResult testResult : testResults) {
                        System.out.println("Test ID: " + testResult.getTest().getId() + " | Rating: " + testResult.getRating() + " | User Answer: " + testResult.getUserAnswer());
                    }
                    break;
                case "6":

                    System.out.println("Укажите путь до набора английских предложений: ");
                    String englishPath = scanner.next();
                    System.out.println("Укажите путь до набора русских предложений: ");
                    String russianPath = scanner.next();
                    testDao.importDataFromCsv(englishPath, russianPath);
                    System.out.println("Tests imported from CSV file");
                    break;
                case "7":
                    System.out.println("Enter XML file path");
                    String xmlFilePath = scanner.next();
                    testDao.exportTestsToXml(testDao.getAllTests(), xmlFilePath);
                    System.out.println("Tests exported to XML file");
                    break;
            }
        }

        scanner.close();
    }



}
