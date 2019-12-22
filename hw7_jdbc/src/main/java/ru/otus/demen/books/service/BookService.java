package ru.otus.demen.books.service;

import ru.otus.demen.books.model.Book;

public interface BookService {
    Book addBook(String name, long authorId, String genreName) throws ServiceError;
}
