package ru.otus.demen.books.dao;

import ru.otus.demen.books.model.BookComment;


public interface BookRepositoryCustom {
    long removeCommentById(String commentId);
    void addComment(String bookId, BookComment bookComment);
}
