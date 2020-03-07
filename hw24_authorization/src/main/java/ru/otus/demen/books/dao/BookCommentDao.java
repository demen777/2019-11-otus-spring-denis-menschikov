package ru.otus.demen.books.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.otus.demen.books.model.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentDao extends JpaRepository<BookComment, Long> {
    @EntityGraph(value = "BookComment.book")
    List<BookComment> findByBookId(long bookId);
    long removeById(Long id);

    Optional<BookComment> findByBookIdAndId(long bookId, long bookCommentId);

    @Modifying
    @Query("delete from BookComment bc where bc.book.id = :id")
    void deleteByBookId(long id);
}
