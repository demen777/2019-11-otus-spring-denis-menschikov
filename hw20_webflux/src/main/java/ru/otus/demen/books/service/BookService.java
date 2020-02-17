package ru.otus.demen.books.service;

import ru.otus.demen.books.model.Book;

import java.util.Arrays;
import java.util.List;

public interface BookService {
    Book add(String name, String authorId, String genreName);
    List<Book> findBySurname(String surname);
    Book getById(String id);
    List<Book> findAll();
    void deleteById(String id);
    void update(String id, String name, String authorId, String genreId);
}
