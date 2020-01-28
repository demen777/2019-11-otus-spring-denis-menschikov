package ru.otus.demen.books.dao;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;

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

    @Override
    public long removeCommentById(String commentId) {
        DeleteResult deleteResult =
                mongoTemplate.remove(new Query(Criteria.where("id").is(commentId)), BookComment.class);
        mongoTemplate.updateFirst(new Query(),
                new Update().pull("comments", Query.query(Criteria.where("$id").is(new ObjectId(commentId)))),
                Book.class);
        return deleteResult.getDeletedCount();
    }

    @Override
    public void addComment(String bookId, BookComment bookComment) {
        mongoTemplate.insert(bookComment);
        UpdateResult updateResult = mongoTemplate.updateFirst(new Query(Criteria.where("id").is(bookId)),
                new Update().push("comments", bookComment),
                Book.class);
        if (updateResult.getModifiedCount() == 0) {
            mongoTemplate.remove(bookComment);
            throw new IdNotFoundException(String.format("No exists book with id=%s", bookId));
        }
    }
}
