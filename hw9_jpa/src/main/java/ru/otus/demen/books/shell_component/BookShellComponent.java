package ru.otus.demen.books.shell_component;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.service.BookService;
import ru.otus.demen.books.service.exception.ServiceException;
import ru.otus.demen.books.view.BookCommentsShellView;
import ru.otus.demen.books.view.BookShellView;
import ru.otus.demen.books.view.BooksShellView;

import java.util.Collection;

@ShellComponent
@RequiredArgsConstructor
public class BookShellComponent {
    private final BookService bookService;
    private final BookShellView bookView;
    private final BooksShellView booksView;
    private final BookCommentsShellView bookCommentsView;

    @ShellMethod(value = "Add book", key = {"add-book"})
    public String addBook(@ShellOption(value = "name") String name,
                          @ShellOption(value = "author-id") long authorId,
                          @ShellOption(value = "genre") String genre) {
        try {
            Book book = bookService.add(name, authorId, genre);
            return bookView.getView(book);
        }
        catch(ServiceException error) {
            return error.getMessage();
        }
    }

    @ShellMethod(value = "Get book by id", key = {"get-book-by-id"})
    public String getBooksById(@ShellOption(value = "id") long id) {
        try {
            Book book = bookService.getById(id);
            return bookView.getView(book);
        }
        catch(ServiceException error) {
            return error.getMessage();
        }
    }

    @ShellMethod(value = "Find books by author's surname", key = {"find-books-by-surname"})
    public String findBooksBySurname(@ShellOption(value = "surname") String surname) {
        try {
            Collection<Book> books = bookService.findBySurname(surname);
            return booksView.getView(books);
        }
        catch(ServiceException error) {
            return error.getMessage();
        }
    }

    @ShellMethod(value = "Show comments for book", key = "show-comments-for-book")
    String findCommentsByBook(@ShellOption(value = "book_id") long bookId) {
        try {
            Book book = bookService.getById(bookId);
            return bookCommentsView.getView(book);
        }
        catch (ServiceException e) {
            return e.getMessage();
        }
    }
}
