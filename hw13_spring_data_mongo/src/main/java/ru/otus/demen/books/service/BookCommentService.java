package ru.otus.demen.books.service;

import ru.otus.demen.books.model.BookComment;

import java.util.Collection;

public interface BookCommentService {
    BookComment add(String bookId, String text);
    boolean deleteById(String bookCommentId);
    Collection<BookComment> getByBookId(String bookId);
}
