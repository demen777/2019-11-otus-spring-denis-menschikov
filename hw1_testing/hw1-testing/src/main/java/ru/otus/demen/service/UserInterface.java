package ru.otus.demen.service;

interface UserInterface {
    String getStudentName();

    String getStudentAnswer(String question);

    void showTestingResult(String studentName, int successTestCounter, int totalTestCounter);
}
