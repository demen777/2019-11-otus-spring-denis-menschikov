package ru.otus.demen.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import ru.otus.demen.model.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvTestProviderTest {
    @org.junit.jupiter.api.Test
    void getTests() {
        Resource resource = new ClassPathResource("/tests_for_tests.csv");
        TestProvider testProvider = new CsvTestProvider(resource);
        List<Test> tests = testProvider.getTests();
        List<Test> expectedTests = List.of(new Test("Красный по английски", "red"),
            new Test("Синий по английски", "blue"));
        assertIterableEquals(expectedTests, tests);
    }
}