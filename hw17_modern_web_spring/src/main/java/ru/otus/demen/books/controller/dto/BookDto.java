package ru.otus.demen.books.controller.dto;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class BookDto {
    private final long id;
    private final String name;
    private final AuthorDto author;
    private final GenreDto genre;
}
