package ru.otus.demen.books.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class Book {
    @Id
    private long id;

    @NonNull
    private String name;

    @NonNull
    private Author author;

    @NonNull
    private Genre genre;

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
