package ru.otus.demen.books.dao;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;


@Repository
@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Long> removeCommentById(String commentId) {
        Mono<DeleteResult> deleteResult =
                mongoTemplate.remove(new Query(Criteria.where("id").is(commentId)), BookComment.class);
        mongoTemplate.updateMulti(new Query(),
                new Update().pull("comments", Query.query(Criteria.where("$id").is(new ObjectId(commentId)))),
                Book.class);
        return deleteResult.map(DeleteResult::getDeletedCount);
    }

    @Override
    public Mono<Void> addComment(String bookId, BookComment bookComment) {
        mongoTemplate.insert(bookComment);
        Mono<UpdateResult> updateResult = mongoTemplate.updateFirst(new Query(Criteria.where("id").is(bookId)),
                new Update().push("comments", bookComment),
                Book.class);
        return updateResult.flatMap(result -> {
            if(result.getModifiedCount() == 0) {
                mongoTemplate.remove(bookComment);
                return Mono.error(new IdNotFoundException(String.format("No exists book with id=%s", bookId)));
            }
            return Mono.empty();
        });
    }
}