package ru.otus.demen.books.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.demen.books.model.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class BookDaoJpa implements BookDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Book save(Book book) {
        if (book.getId() <= 0) {
            em.persist(book);
            return book;
        }
        else {
            return em.merge(book);
        }
    }

    @Override
    public Collection<Book> findBySurname(String surname) {
        TypedQuery<Book> query = em.createQuery(
                "select b from Book b join b.author a where a.surname = :surname", Book.class);
        query.setParameter("surname", surname);
        return query.getResultList();
    }

    @Override
    public Optional<Book> findById(long id) {
        Book book = em.find(Book.class, id);
        return Optional.ofNullable(book);
    }
}
