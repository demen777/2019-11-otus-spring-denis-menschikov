package ru.otus.demen.books.mapper;

import org.mapstruct.Mapper;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Genre;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    ru.otus.demen.books.mongo_model.Genre toMongo(Genre genre);
}
