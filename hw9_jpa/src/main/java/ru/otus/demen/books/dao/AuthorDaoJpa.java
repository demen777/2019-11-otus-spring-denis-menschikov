package ru.otus.demen.books.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.otus.demen.books.model.Author;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class AuthorDaoJpa implements AuthorDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Author> findById(long id) {
        Author author = em.find(Author.class, id);
        return Optional.ofNullable(author);
    }

    @Override
    public Author save(Author author) {
        if (author.getId() <= 0) {
            em.persist(author);
            return author;
        }
        else {
            return em.merge(author);
        }
    }

    @Override
    public Collection<Author> getAll() {
        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public Optional<Author> findByNameAndSurname(String firstName, String surname) {
        TypedQuery<Author> query = em.createQuery(
                "select a from Author a where firstName = :firstName and surname = :surname",
                Author.class);
        query.setParameter("firstName", firstName);
        query.setParameter("surname", surname);
        try {
            return Optional.of(query.getSingleResult());
        }
        catch (NoResultException e)
        {
            return Optional.empty();
        }
    }
}
