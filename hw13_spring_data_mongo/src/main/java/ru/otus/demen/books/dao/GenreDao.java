package ru.otus.demen.books.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.demen.books.model.Genre;

import java.util.Optional;


public interface GenreDao extends MongoRepository<Genre, Long> {
    Optional<Genre> findByName(String name);
}
