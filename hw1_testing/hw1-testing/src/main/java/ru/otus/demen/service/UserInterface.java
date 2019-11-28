package ru.otus.demen.service;

import ru.otus.demen.model.Test;

import java.util.List;

interface UserInterface {
    String getStudentName();
    List<String> getStudentAnswers(List<Test> tests);
    void showTestingResult(String studentName, int successTestCounter, int totalTestCounter);
}
