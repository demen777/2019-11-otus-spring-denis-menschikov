package ru.otus.demen.books.mongo_model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Document
public class BookComment {
    @Id
    private String id;

    @NonNull
    private String text;
}
