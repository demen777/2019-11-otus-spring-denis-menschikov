package ru.otus.demen.books.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.demen.books.model.Book;

import java.util.List;

public interface BookDao extends MongoRepository<Book, Long> {
    List<Book> findByAuthorSurname(String surname);
}
