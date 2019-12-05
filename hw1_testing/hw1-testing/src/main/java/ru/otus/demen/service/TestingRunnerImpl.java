package ru.otus.demen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.demen.model.Test;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestingRunnerImpl implements TestingRunner {
    private final TestProvider testProvider;
    private final UserInterface userInterface;
    private final TestResultCalculator testResultCalculator;

    @Override
    public void run() {
        List<Test> tests = testProvider.getTests();
        String studentName = userInterface.getStudentName();
        List<String> answers = userInterface.getStudentAnswers(tests);
        int successTestCounter = testResultCalculator.checkTests(tests, answers);
        userInterface.showTestingResult(studentName, successTestCounter, tests.size());
    }
}
