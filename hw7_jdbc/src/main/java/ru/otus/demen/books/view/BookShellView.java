package ru.otus.demen.books.view;

import org.springframework.stereotype.Service;
import ru.otus.demen.books.model.Book;

@Service
public class BookShellView implements StringView<Book> {
    @Override
    public String getView(Book book) {
        return String.format("Книга(id=%d): %s автора %s %s в жанре %s", book.getId(), book.getName(),
            book.getAuthor().getFirstName(), book.getAuthor().getSurname(), book.getGenre().getName());
    }
}