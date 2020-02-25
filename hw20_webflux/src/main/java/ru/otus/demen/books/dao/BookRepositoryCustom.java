package ru.otus.demen.books.dao;

import reactor.core.publisher.Mono;
import ru.otus.demen.books.model.BookComment;


public interface BookRepositoryCustom {
    Mono<Long> removeCommentById(String commentId);
    Mono<BookComment> addComment(String bookId, BookComment bookComment);
}
