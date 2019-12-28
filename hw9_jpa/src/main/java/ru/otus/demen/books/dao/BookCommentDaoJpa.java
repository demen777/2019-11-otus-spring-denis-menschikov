package ru.otus.demen.books.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.BookComment;

import javax.persistence.*;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class BookCommentDaoJpa implements BookCommentDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public BookComment save(BookComment bookComment) {
        if (bookComment.getId() <= 0) {
            em.persist(bookComment);
            return bookComment;
        }
        else {
            return em.merge(bookComment);
        }
    }

    @Override
    public Collection<BookComment> findByBookId(long bookId) {
        TypedQuery<BookComment> query = em.createQuery(
                "select bc from BookComment bc join bc.book b where b.id = :bookId",
                BookComment.class);
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }

    @Override
    public int deleteById(long id) {
        Query query = em.createQuery("delete from BookComment bc where bc.id = :id");
        query.setParameter("id", id);
        return query.executeUpdate();
    }
}
