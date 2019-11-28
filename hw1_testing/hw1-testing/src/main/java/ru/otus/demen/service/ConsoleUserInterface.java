package ru.otus.demen.service;

import lombok.RequiredArgsConstructor;
import ru.otus.demen.model.Test;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public class ConsoleUserInterface implements UserInterface {
    private final IOService ioService;

    @Override
    public String getStudentName() {
        ioService.println("Введите ваше имя: ");
        return ioService.getNextLine();
    }

    @Override
    public List<String> getStudentAnswers(List<Test> tests) {
        List<String> answers = new ArrayList<>();
        for(Test test: tests) {
            answers.add(getStudentAnswer(test.getQuestion()));
        }
        return answers;
    }


    private String getStudentAnswer(String question) {
        ioService.println("\nВопрос: " + question + "?");
        return ioService.getNextLine();
    }

    @Override
    public void showTestingResult(String studentName, int successTestCounter, int totalTestCounter) {
        ioService.println("\nТестируемый " + studentName + "\n" +
            "Успешно пройдено " + successTestCounter + " тестов из " + totalTestCounter);
    }
}
