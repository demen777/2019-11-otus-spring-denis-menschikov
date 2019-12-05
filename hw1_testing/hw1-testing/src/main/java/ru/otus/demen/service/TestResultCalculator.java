package ru.otus.demen.service;

import org.springframework.stereotype.Service;
import ru.otus.demen.model.Test;

import java.util.List;

@Service
class TestResultCalculator {
    int checkTests(List<Test> tests, List<String> answers) {
        int successTestCounter = 0;
        for (int i = 0; i < tests.size(); i++) {
            if (tests.get(i).getExpectedAnswer().toUpperCase().equals(answers.get(i).toUpperCase())) {
                successTestCounter++;
            }
        }
        return successTestCounter;
    }
}
