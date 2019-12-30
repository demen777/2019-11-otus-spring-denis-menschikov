package ru.otus.demen.books.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.demen.books.dao.GenreDao;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.exception.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = ServiceTestConfiguration.class)
class GenreServiceImplTest {
    private static final String NOVEL_GENRE_NAME = "Роман";
    private static final long NOVEL_GENRE_ID = 1L;
    private static final String WRONG_NOVEL_GENRE_NAME = "Чугун";

    private Genre novelGenre;

    @Autowired
    GenreService genreService;

    @Autowired
    GenreDao genreDao;

    @BeforeEach
    void setUp() {
        novelGenre = new Genre(NOVEL_GENRE_NAME);
        novelGenre.setId(NOVEL_GENRE_ID);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    @DisplayName("Успешный поиск методом findByName")
    void findByName_ok() {
        when(genreDao.findByName(NOVEL_GENRE_NAME)).thenReturn(Optional.of(novelGenre));
        Optional<Genre> genre = genreService.findByName(NOVEL_GENRE_NAME);
        assertThat(genre.get()).isEqualTo(novelGenre);
    }

    @Test
    @DisplayName("Поиск методом findByName не нашел жанр")
    void findByName_genreNotFoundByName() {
        when(genreDao.findByName(NOVEL_GENRE_NAME)).thenReturn(Optional.empty());
        Optional<Genre> genre = genreService.findByName(NOVEL_GENRE_NAME);
        assertThat(genre).isEmpty();
    }

    @Test
    @DisplayName("Успешное получение жанра методом getByName")
    void getByName_ok() {
        when(genreDao.findByName(WRONG_NOVEL_GENRE_NAME)).thenReturn(Optional.of(novelGenre));
        Genre genre = genreService.getByName(WRONG_NOVEL_GENRE_NAME);
        assertThat(genre).isEqualTo(novelGenre);
    }

    @Test
    @DisplayName("Получение жанра методом getByName выбросило исключение ServiceError ввиду отсуствия жанра")
    void getByName_genreNotFoundByName() {
        when(genreDao.findByName(WRONG_NOVEL_GENRE_NAME)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> genreService.getByName(WRONG_NOVEL_GENRE_NAME))
                .isInstanceOf(NotFoundException.class).hasMessageContaining("Не найден жанр");
    }

    @Test
    @DisplayName("При поиске жанра произошло DataAccessException исключение в GenreDao")
    void findByName_genreDaoThrowDataAccessException() {
        when(genreDao.findByName(NOVEL_GENRE_NAME))
                .thenThrow(new DataAccessResourceFailureException("DataAccessResourceFailureException!!!"));
        assertThatThrownBy(() -> genreService.findByName(NOVEL_GENRE_NAME))
                .isInstanceOf(DataAccessServiceException.class);
    }

    @Test
    @DisplayName("Успешное добавление жанра")
    void add_ok() {
        when(genreDao.save(new Genre(NOVEL_GENRE_NAME))).thenReturn(novelGenre);
        Genre genre = genreService.add(NOVEL_GENRE_NAME);
        assertThat(genre).isEqualTo(novelGenre);
    }

    @Test
    @DisplayName("Исключение при добавлении пустого имени жанра")
    void add_emptyName() {
        assertThatThrownBy(() -> genreService.add("")).isInstanceOf(IllegalParameterException.class)
                .hasMessageStartingWith("Имя жанра должно быть непустым");
    }

    @Test
    @DisplayName("Исключение при добавлении жанра который уже есть в БД")
    void add_alreadyExists() {
        when(genreDao.findByName(NOVEL_GENRE_NAME)).thenReturn(Optional.of(novelGenre));
        assertThatThrownBy(() -> genreService.add(NOVEL_GENRE_NAME))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageStartingWith(String.format("Жанр с именем %s уже есть в БД", NOVEL_GENRE_NAME));
    }

    @Test
    @DisplayName("Получение списка всех жанров")
    void getAll_ok() {
        when(genreDao.getAll()).thenReturn(List.of(novelGenre));
        Collection<Genre> genres = genreService.getAll();
        assertThat(genres).containsExactlyInAnyOrderElementsOf(List.of(novelGenre));
    }
}