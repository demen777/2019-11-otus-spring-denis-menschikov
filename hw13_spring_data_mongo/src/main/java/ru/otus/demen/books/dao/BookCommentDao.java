package ru.otus.demen.books.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.demen.books.model.BookComment;

import java.util.List;

public interface BookCommentDao extends MongoRepository<BookComment, Long> {
    List<BookComment> findByBookId(long bookId);
    long removeById(Long id);
}
