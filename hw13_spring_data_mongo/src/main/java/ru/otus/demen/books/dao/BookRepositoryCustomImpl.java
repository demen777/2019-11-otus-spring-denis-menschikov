package ru.otus.demen.books.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Override
    public List<Book> findByAuthorSurname(String surname) {
        List<Author> authors = mongoTemplate.find(Query.query(Criteria.where("surname").is(surname)), Author.class);
        @SuppressWarnings("UnnecessaryLocalVariable")
        List<Book> books = mongoTemplate.find(
                Query.query(Criteria.where("author").in(authors.toArray(new Object[0]))), Book.class);
        return books;
    }
}
