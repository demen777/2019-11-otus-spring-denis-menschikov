package ru.otus.demen.books.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.demen.books.model.Book;

import java.util.List;

public interface BookDao extends JpaRepository<Book, Long> {
    List<Book> findByAuthorSurname(String surname);
}
