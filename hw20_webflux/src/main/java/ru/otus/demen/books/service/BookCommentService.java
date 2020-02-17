package ru.otus.demen.books.service;

import ru.otus.demen.books.model.BookComment;

import java.util.Arrays;
import java.util.List;

public interface BookCommentService {
    BookComment add(String bookId, String text);
    boolean deleteById(String bookCommentId);
    List<BookComment> getByBookId(String bookId);
}
