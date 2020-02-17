package ru.otus.demen.books.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.demen.books.model.Book;

import java.util.List;

public interface BookDao extends ReactiveMongoRepository<Book, String>, BookRepositoryCustom {
    List<Book> findByAuthorSurname(String surname);
    long countById(String id);
}
