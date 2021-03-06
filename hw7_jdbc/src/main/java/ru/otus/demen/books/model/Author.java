package ru.otus.demen.books.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Author {
    private long id;
    @NonNull
    private String firstName;
    @NonNull
    private String surname;
}
