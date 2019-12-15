package ru.otus.demen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.demen.model.Question;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserInterfaceService implements UserInterface {
    private final IOService ioService;
    private final LocalizedMessageService messageService;

    @Override
    public String getStudentName() {
        ioService.println(messageService.getMessage("input.name") + " ");
        return ioService.getNextLine();
    }

    @Override
    public List<String> getStudentAnswers(List<Question> questions) {
        List<String> answers = new ArrayList<>();
        for(Question question : questions) {
            answers.add(getStudentAnswer(question.getQuestion()));
        }
        return answers;
    }


    private String getStudentAnswer(String question) {
        ioService.println("\n" + messageService.getMessage("question", question));
        return ioService.getNextLine();
    }

    @Override
    public void showTestingResult(String studentName, int successTestCounter, int totalTestCounter) {
        ioService.println("\n" + messageService.getMessage("output.name", studentName));
        ioService.println(messageService.getMessage("results",
                Integer.toString(successTestCounter), Integer.toString(totalTestCounter)));
    }
}
