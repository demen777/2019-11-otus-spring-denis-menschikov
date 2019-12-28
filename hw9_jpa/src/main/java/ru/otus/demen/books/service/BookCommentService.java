package ru.otus.demen.books.service;

import ru.otus.demen.books.model.BookComment;

import java.util.Collection;

public interface BookCommentService {
    BookComment add(long bookId, String text);
    Collection<BookComment> findByBookId(long bookId);
    boolean deleteById(long bookCommentId);
}
