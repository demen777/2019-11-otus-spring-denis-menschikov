package ru.otus.demen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class Hw5TestingCommands {
    private final TestingRunner testingRunner;
    private final LocalizedMessageService messageService;
    private String studentName;

    @ShellMethod(value = "Input student name", key = {"input-name", "i"})
    public String inputName(@ShellOption(value="student-name", defaultValue = "") String studentName) {
        if ("".equals(studentName)) {
            return messageService.getMessage("must.not.empty.student.name");
        }
        this.studentName = studentName;
        return messageService.getMessage("ok");
    }

    @ShellMethod(value = "Start test", key = {"start-test", "s"})
    @ShellMethodAvailability(value = "isStartTestAvailable")
    public void startTest() {
        testingRunner.run(studentName);
    }

    private Availability isStartTestAvailable() {
        return studentName == null
                ? Availability.unavailable(messageService.getMessage("must.input.name.first"))
                : Availability.available();
    }
}
