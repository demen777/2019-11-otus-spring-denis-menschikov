package ru.otus.demen.books.shell_component;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.service.AuthorService;
import ru.otus.demen.books.service.ServiceError;

import java.util.Collection;

@ShellComponent
@RequiredArgsConstructor
public class AuthorShellComponent {
    private final AuthorService authorService;

    @ShellMethod(value = "Add author", key = {"add-author"})
    public String addAuthor(@ShellOption(value = "first_name") String firstName,
                            @ShellOption(value = "surname") String surname) {
        try {
            Author author = authorService.add(firstName, surname);
            return author.toString();
        }
        catch (ServiceError error) {
            return error.getMessage();
        }
    }

    @ShellMethod(value = "Get all authors", key = {"get-all-authors"})
    public String getAllAuthors() {
        try {
            Collection<Author> authors = authorService.getAll();
            return authors.toString();
        }
        catch (ServiceError error) {
            return error.getMessage();
        }
    }
}
