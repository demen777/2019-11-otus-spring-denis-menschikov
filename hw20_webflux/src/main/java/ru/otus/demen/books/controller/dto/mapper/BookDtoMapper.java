package ru.otus.demen.books.controller.dto.mapper;

import org.mapstruct.Mapper;
import ru.otus.demen.books.controller.dto.BookDto;
import ru.otus.demen.books.model.Book;

@Mapper(componentModel = "spring")
public interface BookDtoMapper {
    BookDto bookDto(Book book);
}
