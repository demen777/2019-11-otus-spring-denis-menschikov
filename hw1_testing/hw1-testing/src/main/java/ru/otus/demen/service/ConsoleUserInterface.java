package ru.otus.demen.service;

import ru.otus.demen.model.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleUserInterface implements UserInterface {
    @Override
    public String getStudentName() {
        System.out.println("Введите ваше имя: ");
        return getNextLine();
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
        System.out.println("\nВопрос: " + question + "\n" + "Введите ответ :");
        return getNextLine();
    }

    @Override
    public void showTestingResult(String studentName, int successTestCounter, int totalTestCounter) {
        System.out.println("\nТестируемый " + studentName + "\n" +
            "Успешно пройдено " + successTestCounter + " тестов из " + totalTestCounter);
    }

    private String getNextLine() {
        try(Scanner scanner = new Scanner(System.in)) {
            return scanner.nextLine();
        }
    }
}
