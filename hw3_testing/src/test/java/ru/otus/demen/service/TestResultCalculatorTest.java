package ru.otus.demen.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.demen.model.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestResultCalculatorTest {
    private TestResultCalculator testResultCalculator;
    private List<Question> questions;

    @BeforeEach
    void setUp() {
        testResultCalculator = new TestResultCalculator();
        questions = List.of(new Question("Красный по английски", "red"),
                new Question("Синий по английски", "blue"));
    }

    @Test
    void checkTests() {
        List<String> answers = List.of("red", "black");
        int successTestCounter = testResultCalculator.checkTests(questions, answers);
        assertEquals(1, successTestCounter);
    }

    @Test
    void checkTests_caseInsensitive() {
        List<String> answers = List.of("Red", "black");
        int successTestCounter = testResultCalculator.checkTests(questions, answers);
        assertEquals(1, successTestCounter);
    }
}