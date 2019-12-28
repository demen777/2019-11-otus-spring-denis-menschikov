package ru.otus.demen.books.model;

import lombok.*;

import javax.persistence.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    private String name;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;
}
