package ru.otus.demen.books.model;

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
    @DBRef
    private Author author;

    @NonNull
    @DBRef
    private Genre genre;

    @DBRef(lazy = true)
    List<BookComment> bookComments = new ArrayList<>();

    public Book(String id, @NonNull String name, @NonNull Author author, @NonNull Genre genre) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.genre = genre;
    }

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
