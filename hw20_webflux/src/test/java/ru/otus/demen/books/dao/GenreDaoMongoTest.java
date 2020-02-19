package ru.otus.demen.books.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.demen.books.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


class GenreDaoMongoTest extends BaseDaoMongoTest {
    private static final String NOVEL_GENRE_NAME = "Роман";
    private Genre novelGenre;
    private static final String NEW_GENRE_NAME = "Сказка";
    private static final String WRONG_NOVEL_GENRE_NAME = "Чугун";

    @Autowired
    private GenreDao genreDao;

    @BeforeEach
    void setUp() {
        novelGenre = new Genre(NOVEL_GENRE_NAME);
        mongoTemplate.dropCollection(Genre.class);
        mongoTemplate.save(novelGenre);
    }


    @Test
    @DisplayName("Успешный поиск по имени")
    void findByName_ok() {
        Optional<Genre> genre = genreDao.findByName(NOVEL_GENRE_NAME).blockOptional();
        assertThat(genre.isPresent()).isTrue();
        assertThat(genre.get()).isEqualTo(novelGenre);
    }

    @Test
    @DisplayName("Поиск по имени не нашел жанр")
    void findByName_notFound() {
        Optional<Genre> genre = genreDao.findByName(WRONG_NOVEL_GENRE_NAME).blockOptional();
        assertThat(genre).isEmpty();
    }

    @Test
    @DisplayName("Добавление жанра успешно")
    void save_ok() {
        Optional<Genre> genre = genreDao.save(new Genre(NEW_GENRE_NAME)).blockOptional();
        assertThat(genre).isPresent();
        Genre genreFromDb = mongoTemplate.findById(genre.get().getId(), Genre.class);
        assertThat(genreFromDb).isNotNull();
        assertThat(genreFromDb).isEqualTo(genre.get());
    }

    @Test
    @DisplayName("Получение списка жанров")
    void getAll() {
        Collection<Genre> genres = genreDao.findAll().collectList().block();
        assertThat(genres).containsExactlyInAnyOrderElementsOf(List.of(novelGenre));
    }
}