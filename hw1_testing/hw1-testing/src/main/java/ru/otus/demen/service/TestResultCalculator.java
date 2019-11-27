package ru.otus.demen.service;

import ru.otus.demen.model.OneTest;

import java.util.List;

class TestResultCalculator {
    int checkTests(List<OneTest> tests, List<String> answers) {
        int successTestCounter = 0;
        for (int i = 0; i < tests.size(); i++) {
            if (tests.get(i).getExpectedAnswer().toUpperCase().equals(answers.get(i).toUpperCase())) {
                successTestCounter++;
            }
        }
        return successTestCounter;
    }
}
