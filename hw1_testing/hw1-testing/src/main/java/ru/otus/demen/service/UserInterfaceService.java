package ru.otus.demen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.demen.model.Test;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserInterfaceService implements UserInterface {
    private final IOService ioService;
    private final LocalizedMessageService messageService;

    @Override
    public String getStudentName() {
        ioService.println(messageService.getMessage("inputName") + " ");
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
        ioService.println("\n" + messageService.getMessage("question", new String[] {question}));
        return ioService.getNextLine();
    }

    @Override
    public void showTestingResult(String studentName, int successTestCounter, int totalTestCounter) {
        ioService.println("\n" + messageService.getMessage("outputName", new String[] {studentName}));
        ioService.println(messageService.getMessage("results",
                new String[] {Integer.toString(successTestCounter), Integer.toString(totalTestCounter)}));
    }
}
