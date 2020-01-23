package ru.otus.demen.books.view;

import org.springframework.stereotype.Service;
import ru.otus.demen.books.model.Author;

@Service
public class AuthorShellView implements TextView<Author> {
    @Override
    public String getView(Author author) {
        return String.format("Автор(id=%d): %s %s", author.getId(), author.getFirstName(), author.getSurname());
    }
}
