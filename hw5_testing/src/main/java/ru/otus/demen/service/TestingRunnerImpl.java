package ru.otus.demen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.demen.dao.TestProvider;
import ru.otus.demen.model.Question;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestingRunnerImpl implements TestingRunner {
    private final TestProvider testProvider;
    private final UserInterface userInterface;
    private final TestResultCalculator testResultCalculator;

    @Override
    public void run(String studentName) {
        List<Question> questions = testProvider.getTests();
        List<String> answers = userInterface.getStudentAnswers(questions);
        int successTestCounter = testResultCalculator.checkTests(questions, answers);
        userInterface.showTestingResult(studentName, successTestCounter, questions.size());
    }
}
