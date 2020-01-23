package ru.otus.demen.books.dao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.demen.books.model.Book;

import java.util.List;


public interface BookDao extends JpaRepository<Book, Long> {
    @EntityGraph(value = "Book[author,genre]")
    List<Book> findByAuthorSurname(String surname);

    @SuppressWarnings("NullableProblems")
    @EntityGraph(value = "Book[author,genre]")
    @Override
    List<Book> findAll();
}
