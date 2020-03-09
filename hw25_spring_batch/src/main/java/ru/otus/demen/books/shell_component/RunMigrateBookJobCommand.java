package ru.otus.demen.books.shell_component;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@RequiredArgsConstructor
@ShellComponent
public class RunMigrateBookJobCommand {
    private final JobOperator jobOperator;
    private static final String MIGRATE_BOOK_JOB_NAME = "MigrateBookJob";

    @SneakyThrows
    @ShellMethod(value = "Run migrate books job", key = "migrate")
    void runMigrateBookJob() {
        Long executionId = jobOperator.startNextInstance(MIGRATE_BOOK_JOB_NAME);
        System.out.println(String.format("Run %s id=%d", MIGRATE_BOOK_JOB_NAME, executionId));
    }
}
