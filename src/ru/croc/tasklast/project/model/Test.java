package ru.croc.tasklast.project.model;

import java.util.List;

public class Test {
    private int id;
    private String englishSentence;
    private String russianSentence;

    private List<String> wordsList;

    public Test() {
    }

    public Test(int id, String englishSentence, String russianSentence, List<String> wordsList) {
        this.id = id;
        this.englishSentence = englishSentence;
        this.russianSentence = russianSentence;
        this.wordsList = wordsList;
    }

    public Test(String englishSentence, String russianSentence, List<String> wordsList) {
        this.id = 0;
        this.englishSentence = englishSentence;
        this.russianSentence = russianSentence;
        this.wordsList = wordsList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRussianSentence() {
        return russianSentence;
    }

    public void setRussianSentence(String russianSentence) {
        this.russianSentence = russianSentence;
    }

    public String getEnglishSentence() {
        return englishSentence;
    }

    public void setEnglishSentence(String englishSentence) {
        this.englishSentence = englishSentence;
    }

    public List<String> getWordsList() {
        return wordsList;
    }

    public void setWordsList(List<String> wordsList) {
        this.wordsList = wordsList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Test test = (Test) o;

        if (getId() != test.getId()) return false;
        if (getRussianSentence() != null ? !getRussianSentence().equals(test.getRussianSentence()) : test.getRussianSentence() != null)
            return false;
        if (getEnglishSentence() != null ? !getEnglishSentence().equals(test.getEnglishSentence()) : test.getEnglishSentence() != null)
            return false;
        return getWordsList() != null ? getWordsList().equals(test.getWordsList()) : test.getWordsList() == null;
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getRussianSentence() != null ? getRussianSentence().hashCode() : 0);
        result = 31 * result + (getEnglishSentence() != null ? getEnglishSentence().hashCode() : 0);
        result = 31 * result + (getWordsList() != null ? getWordsList().hashCode() : 0);
        return result;
    }
}
