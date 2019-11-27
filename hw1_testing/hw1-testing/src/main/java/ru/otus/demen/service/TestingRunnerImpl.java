package ru.otus.demen.service;

import lombok.RequiredArgsConstructor;
import ru.otus.demen.model.OneTest;

import java.util.List;

@RequiredArgsConstructor
public class TestingRunnerImpl implements TestingRunner {
    private final TestProvider testProvider;
    private final UserInterface userInterface;
    private final TestResultCalculator testResultCalculator;

    @Override
    public void run() {
        List<OneTest> tests = testProvider.getTests();
        String studentName = userInterface.getStudentName();
        List<String> answers = userInterface.getStudentAnswers(tests);
        int successTestCounter = testResultCalculator.checkTests(tests, answers);
        userInterface.showTestingResult(studentName, successTestCounter, tests.size());
    }
}
