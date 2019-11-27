package ru.otus.demen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import ru.otus.demen.model.OneTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class CsvTestProvider implements TestProvider {
    private final Resource csvResource;

    @Override
    public List<OneTest> getTests() {
        try (Scanner scanner = new Scanner(csvResource.getInputStream()))
        {
            List<OneTest> tests = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String testStr = scanner.nextLine();
                String[] parts = testStr.split(";");
                tests.add(new OneTest(parts[0], parts[1]));
            }
            return tests;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}