package ru.otus.demen.books.service;

import ru.otus.demen.books.model.BookComment;

import java.util.Collection;

public interface BookCommentService {
    BookComment add(long bookId, String text);
    boolean deleteById(long bookCommentId);
    Collection<BookComment> getByBookId(long bookId);
}
