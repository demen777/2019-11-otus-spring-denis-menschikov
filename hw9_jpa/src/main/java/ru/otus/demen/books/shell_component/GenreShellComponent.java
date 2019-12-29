package ru.otus.demen.books.shell_component;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.GenreService;
import ru.otus.demen.books.view.GenreShellView;
import ru.otus.demen.books.view.GenresShellView;

import java.util.Collection;

@ShellComponent
@RequiredArgsConstructor
public class GenreShellComponent {
    private final GenreService genreService;
    private final GenreShellView genreView;
    private final GenresShellView genresView;

    @ShellMethod(value = "Add genre", key = {"add-genre"})
    public String addGenre(@ShellOption(value = "name") String name) {
        return GetStringOrServiceExceptionMessage.call(()->{
            Genre genre = genreService.add(name);
            return genreView.getView(genre);
        });
    }

    @ShellMethod(value = "Get all genres", key = {"get-all-genres"})
    public String getAllGenres() {
        return GetStringOrServiceExceptionMessage.call(()->{
            Collection<Genre> genres = genreService.getAll();
            return genresView.getView(genres);
        });
    }
}
