package ru.otus.demen.books.dao;

import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;


import java.util.List;


public interface BookRepositoryCustom {
    List<Book> findByAuthorSurname(String surname);
    long removeCommentById(String commentId);
    void addComment(String bookId, BookComment bookComment);
}
