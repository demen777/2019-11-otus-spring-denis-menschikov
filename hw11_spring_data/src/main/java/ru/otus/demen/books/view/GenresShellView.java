package ru.otus.demen.books.view;

import org.springframework.stereotype.Service;
import ru.otus.demen.books.model.Genre;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class GenresShellView implements TextView<Collection<Genre>> {
    @Override
    public String getView(Collection<Genre> genres) {
        return "Список жанров:\n" + genres.stream()
            .map((genre) -> String.format("Жанр(id=%d): %s", genre.getId(), genre.getName()))
                .collect(Collectors.joining("\n"));
    }
}
