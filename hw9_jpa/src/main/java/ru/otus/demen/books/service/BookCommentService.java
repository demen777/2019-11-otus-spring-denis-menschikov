package ru.otus.demen.books.service;

import ru.otus.demen.books.model.BookComment;

public interface BookCommentService {
    BookComment add(long bookId, String text);
    boolean deleteById(long bookCommentId);
}
