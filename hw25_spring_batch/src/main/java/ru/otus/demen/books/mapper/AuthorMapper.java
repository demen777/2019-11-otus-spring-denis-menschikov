package ru.otus.demen.books.mapper;

import org.mapstruct.Mapper;
import ru.otus.demen.books.model.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    ru.otus.demen.books.mongo_model.Author toMongo(Author author);
}
