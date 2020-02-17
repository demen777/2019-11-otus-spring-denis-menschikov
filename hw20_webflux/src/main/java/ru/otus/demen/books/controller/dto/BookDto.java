package ru.otus.demen.books.controller.dto;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class BookDto {
    private final String id;
    private final String name;
    private final AuthorDto author;
    private final GenreDto genre;
}
