package ru.otus.demen.books.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.demen.books.model.Genre;

import java.util.Optional;


public interface GenreDao extends ReactiveMongoRepository<Genre, String> {
    Optional<Genre> findByName(String name);
}
