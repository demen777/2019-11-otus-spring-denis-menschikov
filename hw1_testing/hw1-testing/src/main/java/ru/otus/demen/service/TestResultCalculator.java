package ru.otus.demen.service;

import ru.otus.demen.model.Test;

import java.util.List;

class TestResultCalculator {
    int checkTests(List<Test> tests, List<String> answers) {
        int successTestCounter = 0;
        for (int i = 0; i < tests.size(); i++) {
            if (tests.get(i).getExpectedAnswer().equals(answers.get(i))) {
                successTestCounter++;
            }
        }
        return successTestCounter;
    }
}
