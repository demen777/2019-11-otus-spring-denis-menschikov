package ru.otus.demen.books.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.demen.books.model.Book;

public interface BookService {
    Mono<Book> add(String name, String authorId, String genreName);
    Flux<Book> findBySurname(String surname);
    Mono<Book> getById(String id);
    Flux<Book> findAll();
    Mono<Void> deleteById(String id);
    Mono<Void> update(String id, String name, String authorId, String genreId);
}
