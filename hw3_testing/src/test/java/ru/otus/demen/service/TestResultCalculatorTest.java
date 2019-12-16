package ru.otus.demen.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.demen.model.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestResultCalculatorTest {
    @Autowired
    private TestResultCalculator testResultCalculator;

    @Test
    void checkTests() {
        List<String> answers = List.of("red", "black");
        List<Question> questions = List.of(
            new Question("Красный по английски", "red"),
            new Question("Синий по английски", "blue"));
        int successTestCounter = testResultCalculator.checkTests(questions, answers);
        assertEquals(1, successTestCounter);
    }

    @Test
    void checkTests_caseInsensitive() {
        List<String> answers = List.of("Red", "black");
        List<Question> questions = List.of(
            new Question("Красный по английски", "red"),
            new Question("Синий по английски", "blue"));
        int successTestCounter = testResultCalculator.checkTests(questions, answers);
        assertEquals(1, successTestCounter);
    }
}