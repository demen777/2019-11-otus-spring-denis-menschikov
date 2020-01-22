package ru.otus.demen.books.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.demen.books.model.Author;

import java.util.Optional;

public interface AuthorDao extends MongoRepository<Author, Long> {
    Optional<Author> findByFirstNameAndSurname(String firstName, String surname);
}
