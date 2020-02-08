package ru.otus.demen.books.controller.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthorDto {
    private final long id;
    private final String firstName;
    private final String surname;

    public String getName() { return firstName + " " + surname; }
}
