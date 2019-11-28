package ru.otus.demen.service;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import ru.otus.demen.model.OneTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvTestProviderTest {
    @Test
    void getTests() {
        String cvsExample = "Красный по английски;red\n" + "Синий по английски;blue";
        Resource resource = new ByteArrayResource(cvsExample.getBytes());
        TestProvider testProvider = new CsvTestProvider(resource);
        List<OneTest> tests = testProvider.getTests();
        List<OneTest> expectedTests = List.of(new OneTest("Красный по английски", "red"),
            new OneTest("Синий по английски", "blue"));
        assertIterableEquals(expectedTests, tests);
    }
}