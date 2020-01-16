package ru.otus.demen.books.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.demen.books.model.Book;

import java.util.Collection;
import java.util.Optional;

public interface BookDao extends JpaRepository<Book, Long> {
    Collection<Book> findByAuthorSurname(String surname);
}
