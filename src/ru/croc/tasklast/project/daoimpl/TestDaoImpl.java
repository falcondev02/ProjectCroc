package ru.croc.tasklast.project.daoimpl;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import ru.croc.tasklast.project.dao.TestDao;
import ru.croc.tasklast.project.database.DatabaseUtils;
import ru.croc.tasklast.project.model.Test;
import ru.croc.tasklast.project.model.TestListWrapper;


import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestDaoImpl implements TestDao {

    // D:\\javaProjects\\JavaCourse\\src\\ru\\croc\\tasklast\\resources\\english_sentences.csv
    // D:\\javaProjects\\JavaCourse\\src\\ru\\croc\\tasklast\\resources\\russian_sentences.csv
    // D:\\javaProjects\\JavaCourse\\src\\ru\\croc\\tasklast\\resources\\test.xml

    @Override
    public void importDataFromCsv(String filePathEnglish, String filePathRussian) throws IOException, SQLException {
        try (Connection connection = DatabaseUtils.getConnection();
             var lineReaderEnglish = new BufferedReader(new FileReader(filePathEnglish));
             var lineReaderRussian = new BufferedReader(new FileReader(filePathRussian))) {
            connection.setAutoCommit(false);
            String insertQuery = "INSERT INTO test VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            String lineEnglish;
            String lineRussian;
            lineReaderEnglish.readLine();
            lineReaderRussian.readLine();
            while ((lineEnglish=lineReaderEnglish.readLine()) != null &&
                    (lineRussian = lineReaderRussian.readLine()) != null) {
                String[] english = lineEnglish.split(",");
                String[] russian = lineRussian.split(",");
                String englishId = english[0];
                String russianId = russian[0];
                String englishSentence = english[1];
                String russianSentence = russian[1];
                // добавляем один любой айди неважно какого так как файлы по количеству одинаковые и айди у них одинакове
                statement.setInt(1, Integer.parseInt(englishId));
                statement.setString(2, englishSentence);
                statement.setString(3, russianSentence);
                String[] shuffleEnglishWords = englishSentence.split(" ");
                Collections.shuffle(Arrays.asList(shuffleEnglishWords));
                statement.setArray(4, connection.createArrayOf("VARCHAR", shuffleEnglishWords));
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
        }
    }

    public void exportTestsToXml(List<Test> tests, String outputFile) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TestListWrapper.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            TestListWrapper wrapper = new TestListWrapper();
            wrapper.setTests(tests);

            marshaller.marshal(wrapper, new File(outputFile));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Test createTest(Test test) throws SQLException {
        String insertQuery = "INSERT INTO test VALUES (?,?, ?, ?)";
        Connection connection = DatabaseUtils.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, test.getId());
            statement.setString(2, test.getEnglishSentence());
            statement.setString(3, test.getRussianSentence());
            Array wordsListArray = connection.createArrayOf("VARCHAR", test.getWordsList().toArray());
            statement.setArray(4, wordsListArray);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Не удалось создать тест, ни одна строка не была изменена.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    test.setId(id);
                } else {
                    throw new SQLException("Не удалось создать тест, идентификатор не получен.");
                }
            }
        }
        return test;
    }

    @Override
    public Test getTestById(int id) throws SQLException {
        String selectQuery = "SELECT id, english_sentence, russian_sentence, words_list FROM test WHERE id = ?";
        Connection connection = DatabaseUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Test test = new Test();
                    test.setId(id);
                    test.setEnglishSentence(resultSet.getString("english_sentence"));
                    test.setRussianSentence(resultSet.getString("russian_sentence"));
                    Array wordsArray = resultSet.getArray("words_list");
                    Object[] wordsArrayObj = (Object[]) wordsArray.getArray();
                    String[] wordsList = Arrays.copyOf(wordsArrayObj, wordsArrayObj.length, String[].class);
                    test.setWordsList(Arrays.asList(wordsList));
                    return test;
                } else {
                    return null;
                }
            }
        }
    }
    @Override
    public int getMaxTestId() throws SQLException {
        String query = "SELECT MAX(id) FROM test";
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
    public List<Test> getAllTests() throws SQLException {
        String selectQuery = "SELECT * FROM test";
        Connection connection = DatabaseUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(selectQuery);
             ResultSet resultSet = statement.executeQuery()) {
            List<Test> testList = new ArrayList<>();
            while (resultSet.next()) {
                Test test = new Test();
                test.setId(resultSet.getInt("id"));
                test.setEnglishSentence(resultSet.getString("english_sentence"));
                test.setRussianSentence(resultSet.getString("russian_sentence"));
                Array wordsArray = resultSet.getArray("words_list");
                Object[] wordsArrayObject = (Object[]) wordsArray.getArray();
                String[] wordsList = Arrays.stream(wordsArrayObject)
                        .map(Object::toString)
                        .toArray(String[]::new);
                test.setWordsList(Arrays.asList(wordsList));
                testList.add(test);
            }
            return testList;
        }
    }

    @Override
    public boolean updateTest(Test test) throws SQLException {
        String updateQuery = "UPDATE test SET english_sentence = ?, russian_sentence = ?, words_list = ? WHERE id = ?";
        Connection connection = DatabaseUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setString(1, test.getEnglishSentence());
            statement.setString(2, test.getRussianSentence());
            statement.setArray(3, connection.createArrayOf("VARCHAR", test.getWordsList().toArray()));
            statement.setInt(4, test.getId());
            statement.executeUpdate();
        }
        return true;
    }

    @Override
    public boolean deleteTest(Test test) throws SQLException {
        String deleteQuery = "DELETE FROM test WHERE id = ?";
        Connection connection = DatabaseUtils.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, test.getId());
            statement.executeUpdate();
        }
        return true;
    }

}