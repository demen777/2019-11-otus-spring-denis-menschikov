package ru.otus.demen.books.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BookComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
}
