package ru.otus.demen.books.shell_component;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.service.AuthorService;
import ru.otus.demen.books.service.exception.ServiceException;
import ru.otus.demen.books.view.StringView;

import java.util.Collection;
import java.util.Optional;

@ShellComponent
@RequiredArgsConstructor
public class AuthorShellComponent {
    private final AuthorService authorService;
    private final StringView<Author> authorView;
    private final StringView<Collection<Author>> authorsView;

    @ShellMethod(value = "Add author", key = {"add-author"})
    public String addAuthor(@ShellOption(value = "first_name") String firstName,
                            @ShellOption(value = "surname") String surname) {
        try {
            Author author = authorService.add(firstName, surname);
            return authorView.getView(author);
        } catch (ServiceException error) {
            return error.getMessage();
        }
    }

    @ShellMethod(value = "Get all authors", key = {"get-all-authors"})
    public String getAllAuthors() {
        try {
            Collection<Author> authors = authorService.getAll();
            return authorsView.getView(authors);
        } catch (ServiceException error) {
            return error.getMessage();
        }
    }

    @ShellMethod(value = "Find by first name and surname", key = {"find-author-by-name-and-surname"})
    public String findAuthorByNameAndSurname(@ShellOption(value = "first_name") String firstName,
                                             @ShellOption(value = "surname") String surname) {
        try {
            Optional<Author> author = authorService.findByNameAndSurname(firstName, surname);
            return author.map(authorView::getView)
                .orElse(String.format("Автор по имени %s и фамилии %s не найден", firstName, surname));
        } catch (ServiceException error) {
            return error.getMessage();
        }
    }
}
