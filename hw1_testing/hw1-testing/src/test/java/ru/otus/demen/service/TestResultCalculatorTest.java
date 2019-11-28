package ru.otus.demen.service;


import org.junit.jupiter.api.BeforeEach;
import ru.otus.demen.model.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestResultCalculatorTest {
    private TestResultCalculator testResultCalculator;
    private List<Test> tests;

    @BeforeEach
    void setUp() {
        testResultCalculator = new TestResultCalculator();
        tests = List.of(new Test("Красный по английски", "red"),
                new Test("Синий по английски", "blue"));
    }

    @org.junit.jupiter.api.Test
    void checkTests() {
        List<String> answers = List.of("red", "black");
        int successTestCounter = testResultCalculator.checkTests(tests, answers);
        assertEquals(1, successTestCounter);
    }

    @org.junit.jupiter.api.Test
    void checkTests_caseInsensitive() {
        List<String> answers = List.of("Red", "black");
        int successTestCounter = testResultCalculator.checkTests(tests, answers);
        assertEquals(1, successTestCounter);
    }
}