package ru.otus.demen.books.model;

import lombok.*;

@Data
@AllArgsConstructor
public class Genre {
    private long id;
    @NonNull
    private String name;
}
