package ru.otus.demen.service;

import org.springframework.stereotype.Service;
import ru.otus.demen.model.Question;

import java.util.List;

@Service
class TestResultCalculator {
    int checkTests(List<Question> questions, List<String> answers) {
        int successTestCounter = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getExpectedAnswer().toUpperCase().equals(answers.get(i).toUpperCase())) {
                successTestCounter++;
            }
        }
        return successTestCounter;
    }
}
