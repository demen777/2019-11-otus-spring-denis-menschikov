package ru.otus.demen.books.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.demen.books.model.Book;

public interface BookDao extends ReactiveMongoRepository<Book, String>, BookRepositoryCustom {
    Flux<Book> findByAuthorSurname(String surname);
    Mono<Long> countById(String id);
}
