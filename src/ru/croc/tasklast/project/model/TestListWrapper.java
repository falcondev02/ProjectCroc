package ru.croc.tasklast.project.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


import java.util.List;

@XmlRootElement(name = "tests")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestListWrapper {
    @XmlElement(name = "test")
    private List<Test> tests;

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }
}