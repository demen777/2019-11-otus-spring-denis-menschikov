package ru.otus.demen.service;

import ru.otus.demen.model.Question;

import java.util.List;

interface UserInterface {
    String getStudentName();
    List<String> getStudentAnswers(List<Question> questions);
    void showTestingResult(String studentName, int successTestCounter, int totalTestCounter);
}
