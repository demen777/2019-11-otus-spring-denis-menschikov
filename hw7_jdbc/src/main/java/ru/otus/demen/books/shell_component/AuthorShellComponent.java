package ru.otus.demen.books.shell_component;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.demen.books.helper.HandleExceptionUtils;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.service.AuthorService;
import ru.otus.demen.books.view.ObjectView;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

@ShellComponent
@RequiredArgsConstructor
public class AuthorShellComponent {
    private final AuthorService authorService;
    private final ObjectView<Author> authorView;
    private final ObjectView<Collection<Author>> authorsView;

    @ShellMethod(value = "Add author", key = {"add-author"})
    public String addAuthor(@ShellOption(value = "first_name") String firstName,
                            @ShellOption(value = "surname") String surname) {
        return HandleExceptionUtils.getSupplierOrExceptionMessage(
            () -> {
                Author author = authorService.add(firstName, surname);
                return authorView.getView(author);
            }
        );
    }

    @ShellMethod(value = "Get all authors", key = {"get-all-authors"})
    public String getAllAuthors() {
        return HandleExceptionUtils.getSupplierOrExceptionMessage(
            () -> {
                Collection<Author> authors = authorService.getAll();
                return authorsView.getView(authors);
            }
        );
    }

    @ShellMethod(value = "Find by first name and surname", key = {"find-author-by-name-and-surname"})
    public String findAuthorByNameAndSurname(@ShellOption(value = "first_name") String firstName,
                                             @ShellOption(value = "surname") String surname) {
        return HandleExceptionUtils.getSupplierOrExceptionMessage(
            () -> {
                Optional<Author> author = authorService.findByNameAndSurname(firstName, surname);
                return author.map(authorView::getView)
                    .orElse(String.format("Автор по имени %s и фамилии %s не найден", firstName, surname));
            }
        );
    }
}
