package ru.otus.demen.books.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String name;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Author author;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "genre_id")
    private Genre genre;


    @NonNull
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    private List<BookComment> comments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id &&
                name.equals(book.name) &&
                author.equals(book.author) &&
                genre.equals(book.genre) &&
                comments.equals(book.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, author, genre, comments);
    }
}
