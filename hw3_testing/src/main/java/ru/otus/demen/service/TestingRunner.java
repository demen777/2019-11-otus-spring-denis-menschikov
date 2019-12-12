package ru.otus.demen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.demen.model.Question;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestingRunner {
    private final TestProvider testProvider;
    private final UserInterface userInterface;
    private final TestResultCalculator testResultCalculator;

    public void run() {
        List<Question> questions = testProvider.getTests();
        String studentName = userInterface.getStudentName();
        List<String> answers = userInterface.getStudentAnswers(questions);
        int successTestCounter = testResultCalculator.checkTests(questions, answers);
        userInterface.showTestingResult(studentName, successTestCounter, questions.size());
    }
}
