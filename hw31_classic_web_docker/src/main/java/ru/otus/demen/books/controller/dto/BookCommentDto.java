package ru.otus.demen.books.controller.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BookCommentDto {
    private final long id;
    private final String text;
}
