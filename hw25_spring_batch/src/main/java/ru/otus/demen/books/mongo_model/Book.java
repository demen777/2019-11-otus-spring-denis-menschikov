package ru.otus.demen.books.mongo_model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Document
public class Book {
    @Id
    private String id;

    @NonNull
    private String name;

    @NonNull
    private Author author;

    @NonNull
    private Genre genre;

    @NonNull
    @DBRef
    private List<BookComment> comments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return name.equals(book.name) &&
            author.equals(book.author) &&
            genre.equals(book.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, author, genre);
    }
}
