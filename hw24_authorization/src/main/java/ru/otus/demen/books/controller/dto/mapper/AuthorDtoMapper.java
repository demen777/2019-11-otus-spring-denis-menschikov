package ru.otus.demen.books.controller.dto.mapper;


import org.mapstruct.Mapper;
import ru.otus.demen.books.controller.dto.AuthorDto;
import ru.otus.demen.books.model.Author;

@Mapper(componentModel = "spring")
public interface AuthorDtoMapper {
    AuthorDto toAuthorDto(Author author);
}
