package ru.otus.demen.books.model;

import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Genre {
    private long id;
    @NonNull
    private String name;
}
