package ru.otus.demen.books.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.demen.books.model.Book;

public interface BookDao extends MongoRepository<Book, Long>, BookRepositoryCustom {
}
