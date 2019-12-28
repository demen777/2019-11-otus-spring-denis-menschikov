package ru.otus.demen.books.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.demen.books.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(GenreDaoJpa.class)
class GenreDaoJpaTest {
    private static final String NOVEL_GENRE_NAME = "Роман";
    private static final long NOVEL_GENRE_ID = 1L;
    private static final Genre NOVEL_GENRE = new Genre(NOVEL_GENRE_ID, NOVEL_GENRE_NAME);
    private static final String NEW_GENRE_NAME = "Сказка";
    private static final String WRONG_NOVEL_GENRE_NAME = "Чугун";

    @Autowired
    GenreDao genreDao;

    @Test
    @DisplayName("Успешный поиск по имени")
    void findByName_ok() {
        Optional<Genre> genre = genreDao.findByName(NOVEL_GENRE_NAME);
        assertThat(genre.isPresent()).isTrue();
        assertThat(genre.get()).isEqualTo(NOVEL_GENRE);
    }

    @Test
    @DisplayName("Поиск по имени не нашел жанр")
    void findByName_notFound() {
        Optional<Genre> genre = genreDao.findByName(WRONG_NOVEL_GENRE_NAME);
        assertThat(genre.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Добавление жанра успешно")
    void save_ok() {
        Genre genre = genreDao.save(new Genre(NEW_GENRE_NAME));
        Optional<Genre> genreFromDb = genreDao.findByName(NEW_GENRE_NAME);
        assertThat(genreFromDb.isPresent()).isTrue();
        assertThat(genreFromDb.get()).isEqualTo(genre);
    }

    @Test
    @DisplayName("Получение списка жанров")
    void getAll() {
        Collection<Genre> genres = genreDao.getAll();
        assertThat(genres).containsExactlyInAnyOrderElementsOf(List.of(NOVEL_GENRE));
    }
}