package ru.otus.demen.books.controller.dto;

import lombok.Value;

@Value
public class BookInputDto {
    private final String name;
    private final long authorId;
    private final long genreId;
}
