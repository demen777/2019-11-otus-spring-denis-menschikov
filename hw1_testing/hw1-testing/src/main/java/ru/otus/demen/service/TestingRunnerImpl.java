package ru.otus.demen.service;

import lombok.RequiredArgsConstructor;
import ru.otus.demen.model.Test;

import java.util.List;

@RequiredArgsConstructor
public class TestingRunnerImpl implements TestingRunner {
    private final TestProvider testProvider;
    private final UserInterface userInterface;

    @Override
    public void run() {
        List<Test> tests = testProvider.getTests();
        String studentName = userInterface.getStudentName();
        int successTestCounter = 0;
        for (Test test : tests) {
            String studentAnswer = userInterface.getStudentAnswer(test.getQuestion());
            if (test.getExpectedAnswer().equals(studentAnswer)) {
                successTestCounter++;
            }
        }
        userInterface.showTestingResult(studentName, successTestCounter, tests.size());
    }
}
