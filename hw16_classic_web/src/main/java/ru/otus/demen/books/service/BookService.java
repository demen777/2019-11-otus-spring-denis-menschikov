package ru.otus.demen.books.service;

import ru.otus.demen.books.model.Book;

import java.util.List;

public interface BookService {
    Book add(String name, long authorId, String genreName);
    List<Book> findBySurname(String surname);
    Book getById(long id);
    List<Book> findAll();
    void update(long id, String name, long author_id, long genre_id);
}
