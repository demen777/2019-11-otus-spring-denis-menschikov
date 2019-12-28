package ru.otus.demen.books.view;

import org.springframework.stereotype.Service;
import ru.otus.demen.books.model.Book;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class BooksShellView implements TextView<Collection<Book>> {
    @Override
    public String getView(Collection<Book> books) {
        return "Список книг:\n" + books.stream()
            .map((book) -> String.format("Книга(id=%d): %s автора %s %s в жанре %s", book.getId(), book.getName(),
                book.getAuthor().getFirstName(), book.getAuthor().getSurname(), book.getGenre().getName()))
            .collect(Collectors.joining("\n"));
    }
}