package ru.otus.demen.books.model;

import lombok.*;

import javax.persistence.*;

@NamedEntityGraph(name = "BookComment[book]",
    attributeNodes = {@NamedAttributeNode(value = "book", subgraph = "Book[author,genre]")},
        subgraphs = {@NamedSubgraph(
                name = "Book[author,genre]",
                attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("genre")}
        )}
)
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class BookComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String text;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
