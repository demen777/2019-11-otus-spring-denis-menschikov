package ru.otus.demen.books.service;

import reactor.core.publisher.Mono;
import ru.otus.demen.books.model.BookComment;

public interface BookCommentService {
    Mono<BookComment> add(String bookId, String text);
    Mono<Boolean> deleteById(String bookCommentId);
}
