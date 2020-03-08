package ru.otus.demen.books.mongo_model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Document
public class Author {
    @Id
    private String id;

    @NonNull
    private String firstName;

    @NonNull
    private String surname;
}
