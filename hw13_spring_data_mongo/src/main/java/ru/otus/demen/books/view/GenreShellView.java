package ru.otus.demen.books.view;

import org.springframework.stereotype.Service;
import ru.otus.demen.books.model.Genre;

@Service
public class GenreShellView implements TextView<Genre> {
    @Override
    public String getView(Genre genre) {
        return String.format("Жанр(id=%s): %s", genre.getId(), genre.getName());
    }
}
