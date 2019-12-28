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
public class CommentShellComponent {
    private final BookCommentService bookCommentService;
    private final BookCommentShellView bookCommentShellView;

    @ShellMethod(value = "Add comment to book", key = "add-book-comment")
    String addBookComment(@ShellOption(value = "book_id") long bookId, @ShellOption(value = "comment") String comment){
        BookComment bookComment = bookCommentService.add(bookId, comment);
        return bookCommentShellView.getView(bookComment);
    }
}
