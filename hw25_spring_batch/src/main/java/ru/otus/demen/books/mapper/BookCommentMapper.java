package ru.otus.demen.books.mapper;

import org.mapstruct.Mapper;
import ru.otus.demen.books.model.BookComment;

@Mapper(componentModel = "spring")
public interface BookCommentMapper {
    ru.otus.demen.books.mongo_model.BookComment toMongo(BookComment bookComment);
}
