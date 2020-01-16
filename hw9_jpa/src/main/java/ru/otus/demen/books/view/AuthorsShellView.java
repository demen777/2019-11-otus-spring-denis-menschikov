package ru.otus.demen.books.view;

import org.springframework.stereotype.Service;
import ru.otus.demen.books.model.Author;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class AuthorsShellView implements TextView<Collection<Author>> {
    @Override
    public String getView(Collection<Author> authors) {
        return "Список авторов:\n" + authors.stream()
            .map((author) -> String.format(
                "Автор(id=%d): %s %s", author.getId(), author.getFirstName(), author.getSurname()))
            .collect(Collectors.joining("\n"));
    }
}
