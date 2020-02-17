package ru.otus.demen.books.controller.dto.mapper;

import org.mapstruct.Mapper;
import ru.otus.demen.books.controller.dto.BookCommentDto;
import ru.otus.demen.books.model.BookComment;

@Mapper(componentModel = "spring")
public interface BookCommentDtoMapper {
    BookCommentDto toBookCommentDto(BookComment bookComment);
}
