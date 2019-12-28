package ru.otus.demen.books.shell_component;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.service.BookCommentService;
import ru.otus.demen.books.service.exception.ServiceException;
import ru.otus.demen.books.view.TextView;

import java.util.Collection;

@ShellComponent
@RequiredArgsConstructor
public class BookCommentShellComponent {
    private final BookCommentService bookCommentService;
    private final TextView<BookComment> bookCommentShellView;
    private final TextView<Collection<BookComment>> bookCommentsShellView;
    private final

    @ShellMethod(value = "Add comment to book", key = "add-book-comment")
    String addBookComment(@ShellOption(value = "book_id") long bookId, @ShellOption(value = "comment") String comment){
        try {
            BookComment bookComment = bookCommentService.add(bookId, comment);
            return bookCommentShellView.getView(bookComment);
        }
        catch (ServiceException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Find comments by book", key = "find-comments-by-book")
    String findCommentsByBook(@ShellOption(value = "book_id") long bookId) {
        try {
            Collection<BookComment> bookComments = bookCommentService.findByBookId(bookId);
            return bookCommentsShellView.getView(bookComments);
        }
        catch (ServiceException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Delete book comment", key = "delete-book-comment")
    String deleteBookComment(@ShellOption("book_comment_id") long bookCommentId) {
        try {
            if (bookCommentService.deleteById(bookCommentId)) {
                return "OK";
            }
            else {
                return String.format("Комментарий с id=%d отсуствует в хранилище", bookCommentId);
            }
        }
        catch (ServiceException e) {
            return e.getMessage();
        }
    }
}
