package ru.otus.demen.books.shell_component;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.service.BookCommentService;
import ru.otus.demen.books.service.BookService;
import ru.otus.demen.books.view.BookCommentShellView;
import ru.otus.demen.books.view.BookCommentsShellView;

@ShellComponent
@RequiredArgsConstructor
public class BookCommentShellComponent {
    private final BookService bookService;
    private final BookCommentService bookCommentService;
    private final BookCommentShellView bookCommentShellView;
    private final BookCommentsShellView bookCommentsView;

    @ShellMethod(value = "Add comment to book", key = "add-book-comment")
    String addBookComment(@ShellOption(value = "book_id") String bookId, @ShellOption(value = "comment") String comment){
        return GetStringOrServiceExceptionMessage.call(()->{
            BookComment bookComment = bookCommentService.add(bookId, comment);
            return bookCommentShellView.getView(bookComment);
        });
    }

    @ShellMethod(value = "Delete book comment", key = "delete-book-comment")
    String deleteBookComment(@ShellOption("book_comment_id") String bookCommentId) {
        return GetStringOrServiceExceptionMessage.call(()->{
            if (bookCommentService.deleteById(bookCommentId)) {
                return "OK";
            }
            else {
                return String.format("Комментарий с id=%s отсуствует в хранилище", bookCommentId);
            }
        });
    }

    @ShellMethod(value = "Show comments for book", key = "show-comments-for-book")
    String findCommentsByBook(@ShellOption(value = "book_id") String bookId) {
        return GetStringOrServiceExceptionMessage.call(()->{
            Book book = bookService.getById(bookId);
            return bookCommentsView.getView(book);
        });
    }
}
