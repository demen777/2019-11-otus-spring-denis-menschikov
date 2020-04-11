package ru.otus.demen.books.controller.dto.mapper;

import org.mapstruct.Mapper;
import ru.otus.demen.books.controller.dto.GenreDto;
import ru.otus.demen.books.model.Genre;

@Mapper(componentModel = "spring")
public interface GenreDtoMapper {
    GenreDto toGenreDto(Genre genre);
}
