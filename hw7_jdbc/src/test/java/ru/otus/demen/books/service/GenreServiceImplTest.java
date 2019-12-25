package ru.otus.demen.books.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessResourceFailureException;
import ru.otus.demen.books.dao.GenreDao;
import ru.otus.demen.books.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = GenreServiceImpl.class)
class GenreServiceImplTest {
    private static final String NOVEL_GENRE_NAME = "Роман";
    private static final long NOVEL_GENRE_ID = 1L;
    private static final Genre NOVEL_GENRE = new Genre(NOVEL_GENRE_ID, NOVEL_GENRE_NAME);
    private static final String WRONG_NOVEL_GENRE_NAME = "Чугун";

    @Autowired
    GenreService genreService;

    @MockBean
    GenreDao genreDao;

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    @DisplayName("Успешный поиск методом findByName")
    void findByName_ok() throws ServiceError {
        when(genreDao.findByName(NOVEL_GENRE_NAME)).thenReturn(Optional.of(NOVEL_GENRE));
        Optional<Genre> genre = genreService.findByName(NOVEL_GENRE_NAME);
        assertThat(genre.get()).isEqualTo(NOVEL_GENRE);
    }

    @Test
    @DisplayName("Поиск методом findByName не нашел жанр")
    void findByName_genreNotFoundByName() throws ServiceError {
        when(genreDao.findByName(NOVEL_GENRE_NAME)).thenReturn(Optional.empty());
        Optional<Genre> genre = genreService.findByName(NOVEL_GENRE_NAME);
        assertThat(genre.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Успешное получение жанра методом getByName")
    void getByName_ok() throws ServiceError {
        when(genreDao.findByName(WRONG_NOVEL_GENRE_NAME)).thenReturn(Optional.of(NOVEL_GENRE));
        Genre genre = genreService.getByName(WRONG_NOVEL_GENRE_NAME);
        assertThat(genre).isEqualTo(NOVEL_GENRE);
    }

    @Test
    @DisplayName("Получение жанра методом getByName выбросило исключение ServiceError ввиду отсуствия жанра")
    void getByName_genreNotFoundByName() {
        when(genreDao.findByName(WRONG_NOVEL_GENRE_NAME)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> genreService.getByName(WRONG_NOVEL_GENRE_NAME))
                .isInstanceOf(ServiceError.class).hasMessageContaining("Не найден жанр");
    }

    @Test
    @DisplayName("При поиске жанра произошло DataAccessException исключение в GenreDao")
    void findByName_genreDaoThrowDataAccessException() {
        when(genreDao.findByName(NOVEL_GENRE_NAME))
                .thenThrow(new DataAccessResourceFailureException("DataAccessResourceFailureException!!!"));
        assertThatThrownBy(() -> genreService.findByName(NOVEL_GENRE_NAME))
                .isInstanceOf(ServiceError.class).hasMessageContaining("Ошибка Dao во время поиска жанра по имени");
    }

    @Test
    @DisplayName("Успешное добавление жанра")
    void add_ok() throws ServiceError {
        when(genreDao.save(new Genre(NOVEL_GENRE_NAME))).thenReturn(NOVEL_GENRE);
        Genre genre = genreService.add(NOVEL_GENRE_NAME);
        assertThat(genre).isEqualTo(NOVEL_GENRE);
    }

    @Test
    @DisplayName("Исключение при добавлении пустого имени жанра")
    void add_emptyName() {
        assertThatThrownBy(() -> genreService.add("")).isInstanceOf(ServiceError.class)
                .hasMessageStartingWith("Имя жанра должно быть непустым");
    }

    @Test
    @DisplayName("Исключение при добавлении жанра который уже есть в БД")
    void add_alreadyExists() {
        when(genreDao.findByName(NOVEL_GENRE_NAME)).thenReturn(Optional.of(NOVEL_GENRE));
        assertThatThrownBy(() -> genreService.add(NOVEL_GENRE_NAME))
                .isInstanceOf(ServiceError.class)
                .hasMessageStartingWith(String.format("Жанр с именем %s уже есть в БД", NOVEL_GENRE_NAME));
    }

    @Test
    @DisplayName("Получение списка всех жанров")
    void getAll_ok() throws ServiceError {
        when(genreDao.getAll()).thenReturn(List.of(NOVEL_GENRE));
        Collection<Genre> genres = genreService.getAll();
        assertThat(genres).containsExactlyInAnyOrderElementsOf(List.of(NOVEL_GENRE));
    }
}