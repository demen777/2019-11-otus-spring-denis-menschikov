package ru.otus.demen.books.shell_component;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.service.BookService;
import ru.otus.demen.books.service.ServiceError;

@ShellComponent
@RequiredArgsConstructor
public class BookShellComponent {
    private BookService bookService;

    @ShellMethod(value = "Add book", key = {"add-book"})
    public String addBook(@ShellOption(value = "name") String name,
                          @ShellOption(value = "author-id") long authorId,
                          @ShellOption(value = "genre") String genre) {
        try {
            Book book = bookService.addBook(name, authorId, genre);
            return book.toString();
        }
        catch(ServiceError error) {
            return error.getMessage();
        }
    }
}
