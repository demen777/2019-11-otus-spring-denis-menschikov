package ru.otus.demen.books.shell_component;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.service.BookCommentService;
import ru.otus.demen.books.view.BookCommentShellView;

@ShellComponent
@RequiredArgsConstructor
public class BookCommentShellComponent {
    private final BookCommentService bookCommentService;
    private final BookCommentShellView bookCommentShellView;

    @ShellMethod(value = "Add comment to book", key = "add-book-comment")
    String addBookComment(@ShellOption(value = "book_id") long bookId, @ShellOption(value = "comment") String comment){
        return GetStringOrServiceExceptionMessage.call(()->{
            BookComment bookComment = bookCommentService.add(bookId, comment);
            return bookCommentShellView.getView(bookComment);
        });
    }

    @ShellMethod(value = "Delete book comment", key = "delete-book-comment")
    String deleteBookComment(@ShellOption("book_comment_id") long bookCommentId) {
        return GetStringOrServiceExceptionMessage.call(()->{
            if (bookCommentService.deleteById(bookCommentId)) {
                return "OK";
            }
            else {
                return String.format("Комментарий с id=%d отсуствует в хранилище", bookCommentId);
            }
        });
    }
}
