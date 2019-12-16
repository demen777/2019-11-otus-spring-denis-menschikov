package ru.otus.demen.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import ru.otus.demen.model.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class CsvTestProvider implements TestProvider {
    private final Resource csvResource;

    @Override
    public List<Question> getTests() {
        try (Scanner scanner = new Scanner(csvResource.getInputStream()))
        {
            List<Question> questions = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String testStr = scanner.nextLine();
                String[] parts = testStr.split(";");
                questions.add(new Question(parts[0], parts[1]));
            }
            return questions;
        } catch (IOException e) {
            throw new CorruptedTestFileError(e);
        }
    }

}
