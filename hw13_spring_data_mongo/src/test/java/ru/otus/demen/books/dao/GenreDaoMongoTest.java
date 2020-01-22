package ru.otus.demen.books.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.demen.books.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


class GenreDaoMongoTest extends BaseDaoMongoTest {
    private static final String NOVEL_GENRE_NAME = "Роман";
    private static final long NOVEL_GENRE_ID = 1L;
    private Genre novelGenre;
    private static final String NEW_GENRE_NAME = "Сказка";
    private static final String WRONG_NOVEL_GENRE_NAME = "Чугун";

    @Autowired
    private GenreDao genreDao;

    @BeforeEach
    void setUp() {
        novelGenre = new Genre(NOVEL_GENRE_NAME);
        novelGenre.setId(NOVEL_GENRE_ID);
    }


    @Test
    @DisplayName("Успешный поиск по имени")
    void findByName_ok() {
        Optional<Genre> genre = genreDao.findByName(NOVEL_GENRE_NAME);
        assertThat(genre.isPresent()).isTrue();
        assertThat(genre.get()).isEqualTo(novelGenre);
    }

    @Test
    @DisplayName("Поиск по имени не нашел жанр")
    void findByName_notFound() {
        Optional<Genre> genre = genreDao.findByName(WRONG_NOVEL_GENRE_NAME);
        assertThat(genre).isEmpty();
    }

    @Test
    @DisplayName("Добавление жанра успешно")
    void save_ok() {
        Genre genre = genreDao.save(new Genre(NEW_GENRE_NAME));
        Genre genreFromDb = mongoTemplate.findById(genre.getId(), Genre.class);
        assertThat(genreFromDb).isNotNull();
        assertThat(genreFromDb).isEqualTo(genre);
    }

    @Test
    @DisplayName("Получение списка жанров")
    void getAll() {
        Collection<Genre> genres = genreDao.findAll();
        assertThat(genres).containsExactlyInAnyOrderElementsOf(List.of(novelGenre));
    }
}