package ru.otus.demen.books.model;

import lombok.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Book {
    private long id;
    @NonNull
    private String name;
    @NonNull
    private Author author;
    @NonNull
    private Genre genre;
}
