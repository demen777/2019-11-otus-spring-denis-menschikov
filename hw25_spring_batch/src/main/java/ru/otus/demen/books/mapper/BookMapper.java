package ru.otus.demen.books.mapper;

import org.mapstruct.Mapper;
import ru.otus.demen.books.mongo_model.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toMongo(ru.otus.demen.books.model.Book book);
}
