package ru.otus.demen.books.view;

import ru.otus.demen.books.model.BookComment;

public class BookCommentShellView implements TextView<BookComment> {
    @Override
    public String getView(BookComment bookComment) {
        return String.format("Комментарий(id=%d): %s", bookComment.getId(), bookComment.getText());
    }
}
