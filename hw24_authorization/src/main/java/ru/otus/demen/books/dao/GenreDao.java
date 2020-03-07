package ru.otus.demen.books.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.demen.books.model.Genre;

import java.util.Optional;


public interface GenreDao extends JpaRepository<Genre, Long> {
    Optional<Genre> findByName(String name);
}
