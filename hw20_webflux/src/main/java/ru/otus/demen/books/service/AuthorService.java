package ru.otus.demen.books.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.demen.books.model.Author;

public interface AuthorService {
    Mono<Author> findById(String id);
    Mono<Author> getById(String id);
    Mono<Author> findByNameAndSurname(String firstName, String surname);
    Mono<Author> add(String firstName, String surname);
    Flux<Author> getAll();
}
