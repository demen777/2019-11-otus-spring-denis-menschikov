package ru.otus.demen.books.view;

import org.springframework.stereotype.Service;
import ru.otus.demen.books.model.Author;

@Service
public class AuthorShellView implements ObjectView<Author> {
    @Override
    public String getView(Author object) {
        return String.format("Автор(id=%d): %s %s", object.getId(), object.getFirstName(), object.getSurname());
    }
}
