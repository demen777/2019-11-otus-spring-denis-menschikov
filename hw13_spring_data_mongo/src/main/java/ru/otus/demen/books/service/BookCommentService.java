package ru.otus.demen.books.service;

import ru.otus.demen.books.model.BookComment;

public interface BookCommentService {
    BookComment add(String bookId, String text);
    boolean deleteById(String bookCommentId);
}
