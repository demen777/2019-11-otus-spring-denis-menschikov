package ru.otus.demen.service;

import ru.otus.demen.model.OneTest;

import java.util.List;

interface UserInterface {
    String getStudentName();
    List<String> getStudentAnswers(List<OneTest> tests);
    void showTestingResult(String studentName, int successTestCounter, int totalTestCounter);
}
