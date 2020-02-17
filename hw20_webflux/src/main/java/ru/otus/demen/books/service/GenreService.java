package ru.otus.demen.books.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.demen.books.model.Genre;

public interface GenreService {
    Mono<Genre> findByName(String name);
    Mono<Genre> getByName(String name);
    Mono<Genre> add(String name);
    Flux<Genre> getAll();
}
