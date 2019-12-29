package ru.otus.demen.books.dao;

import ru.otus.demen.books.model.BookComment;

import java.util.Collection;

public interface BookCommentDao {
    Collection<BookComment> findByBookId(long bookId);
    int deleteById(long bookCommentId);
}
