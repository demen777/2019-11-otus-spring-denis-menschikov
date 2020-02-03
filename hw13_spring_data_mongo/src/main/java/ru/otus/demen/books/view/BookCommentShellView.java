package ru.otus.demen.books.view;

import org.springframework.stereotype.Service;
import ru.otus.demen.books.model.BookComment;

@Service
public class BookCommentShellView implements TextView<BookComment> {
    @Override
    public String getView(BookComment bookComment) {
        return String.format("Комментарий(id=%s): %s", bookComment.getId(), bookComment.getText());
    }
}
