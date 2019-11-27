package ru.otus.demen.service;

import java.util.Scanner;

public class ConsoleUserInterface implements UserInterface {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String getStudentName() {
        System.out.println("Введите ваше имя: ");
        return scanner.nextLine();
    }

    @Override
    public String getStudentAnswer(String question) {
        System.out.println("\nВопрос: " + question + "\n" + "Введите ответ :");
        return scanner.nextLine();
    }

    @Override
    public void showTestingResult(String studentName, int successTestCounter, int totalTestCounter) {
        System.out.println("\nТестируемый " + studentName + "\n" +
            "Успешно пройдено " + successTestCounter + " тестов из " + totalTestCounter);
    }
}
