package ru.otus.demen.service;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import ru.otus.demen.model.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvTestProviderTest {
    @Test
    void getTests() {
        Resource resource = new ClassPathResource("/tests_for_tests_ru_ru.csv");
        TestProvider testProvider = new CsvTestProvider(resource);
        List<Question> questions = testProvider.getTests();
        List<Question> expectedQuestions = List.of(new Question("Красный по английски", "red"),
            new Question("Синий по английски", "blue"));
        assertIterableEquals(expectedQuestions, questions);
    }
}