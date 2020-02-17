package ru.otus.demen.books.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.demen.books.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao extends ReactiveMongoRepository<Author, String> {
    Mono<Author> findByFirstNameAndSurname(String firstName, String surname);
    Flux<Author> findBySurname(String surname);
}
