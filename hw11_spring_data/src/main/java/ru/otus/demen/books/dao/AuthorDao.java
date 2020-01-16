package ru.otus.demen.books.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.demen.books.model.Author;

import java.util.Collection;
import java.util.Optional;

public interface AuthorDao extends JpaRepository<Author, Long> {
    Optional<Author> findByFirstNameAndSurname(String firstName, String surname);
}
