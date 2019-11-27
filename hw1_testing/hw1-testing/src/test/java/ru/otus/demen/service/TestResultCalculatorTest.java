package ru.otus.demen.service;


import org.junit.jupiter.api.Test;
import ru.otus.demen.model.OneTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestResultCalculatorTest {
    private final TestResultCalculator testResultCalculator = new TestResultCalculator();
    private final List<OneTest> tests = List.of(new OneTest("Красный по английски", "red"),
        new OneTest("Синий по английски", "blue"));

    @Test
    void checkTests() {
        List<String> answers = List.of("red", "black");
        int successTestCounter = testResultCalculator.checkTests(tests, answers);
        assertEquals(1, successTestCounter);
    }

    @Test
    void checkTests_caseInsensitive() {
        List<String> answers = List.of("Red", "black");
        int successTestCounter = testResultCalculator.checkTests(tests, answers);
        assertEquals(1, successTestCounter);
    }
}