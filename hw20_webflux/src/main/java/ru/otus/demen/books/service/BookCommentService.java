package ru.otus.demen.books.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.demen.books.model.BookComment;

import java.util.Arrays;
import java.util.List;

public interface BookCommentService {
    Mono<BookComment> add(String bookId, String text);
    Mono<Boolean> deleteById(String bookCommentId);
}
