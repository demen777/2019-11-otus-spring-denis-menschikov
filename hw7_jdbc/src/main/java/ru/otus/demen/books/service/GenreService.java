package ru.otus.demen.books.service;

import ru.otus.demen.books.model.Genre;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface GenreService {
    Optional<Genre> findByName(String name) throws ServiceError;
    Genre getByName(String name) throws ServiceError;
}
