package ru.otus.demen.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TestResultCalculatorTest {
    @Autowired
    private TestResultCalculator testResultCalculator;
    @Autowired
    private TestProvider testProvider;

    @Test
    void checkTests() {
        List<String> answers = List.of("red", "black");
        int successTestCounter = testResultCalculator.checkTests(testProvider.getTests(), answers);
        assertEquals(1, successTestCounter);
    }

    @Test
    void checkTests_caseInsensitive() {
        List<String> answers = List.of("Red", "black");
        int successTestCounter = testResultCalculator.checkTests(testProvider.getTests(), answers);
        assertEquals(1, successTestCounter);
    }
}