package ru.croc.tasklast.project.dao;

import ru.croc.tasklast.project.model.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface TestDao {
    void importDataFromCsv(String englishSentencesFilePath,
                           String russianSentencesFilePath) throws IOException, SQLException;
    void exportTestsToXml(List<Test> tests, String outputFile);
    Test createTest(Test test) throws SQLException, FileNotFoundException;
    Test getTestById(int id) throws SQLException;
    List<Test> getAllTests() throws SQLException;
    boolean updateTest(Test test) throws SQLException;
    boolean deleteTest(Test test) throws SQLException;
    int getMaxTestId() throws SQLException;
}
