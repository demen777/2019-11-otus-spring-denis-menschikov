package ru.otus.demen.books.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.demen.books.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao extends MongoRepository<Author, String> {
    Optional<Author> findByFirstNameAndSurname(String firstName, String surname);
    List<Author> findBySurname(String surname);
}
