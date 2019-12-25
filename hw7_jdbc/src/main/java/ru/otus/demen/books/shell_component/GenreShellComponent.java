package ru.otus.demen.books.shell_component;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.GenreService;
import ru.otus.demen.books.service.ServiceError;

import java.util.Collection;

@ShellComponent
@RequiredArgsConstructor
public class GenreShellComponent {
    private final GenreService genreService;

    @ShellMethod(value = "Add genre", key = {"add-genre"})
    public String addGenre(@ShellOption(value = "name") String name) {
        try {
            Genre genre = genreService.add(name);
            return genre.toString();
        }
        catch (ServiceError error) {
            return error.getMessage();
        }
    }

    @ShellMethod(value = "Get all genres", key = {"get-all-genres"})
    public String getAllGenres() {
        try {
            Collection<Genre> genre = genreService.getAll();
            return genre.toString();
        }
        catch (ServiceError error) {
            return error.getMessage();
        }
    }
}
