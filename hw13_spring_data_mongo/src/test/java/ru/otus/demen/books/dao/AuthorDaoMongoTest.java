package ru.otus.demen.books.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.demen.books.model.Author;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


class AuthorDaoMongoTest extends BaseDaoMongoTest {
    private static final String TOLSTOY_FIRST_NAME = "Лев";
    private static final String TOLSTOY_SURNAME = "Толстой";
    private static final String WRONG_AUTHOR_ID = "-1";
    private static final String DOSTOEVSKY_FIRST_NAME = "Федор";
    private static final String DOSTOEVSKY_SURNAME = "Достоевский";

    private Author newAuthor;
    private Author tolstoyAuthor;

    @Autowired
    AuthorDao authorDao;

    @BeforeEach
    void setUp() {
        tolstoyAuthor = new Author(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME);
        newAuthor = new Author(DOSTOEVSKY_FIRST_NAME, DOSTOEVSKY_SURNAME);
        mongoTemplate.dropCollection(Author.class);
        mongoTemplate.save(tolstoyAuthor);
    }

    @Test
    @DisplayName("Успешный поиск методом findById")
    void findById_ok() {
        Optional<Author> author = authorDao.findById(tolstoyAuthor.getId());
        assertThat(author).isPresent().get().isEqualTo(tolstoyAuthor);
    }

    @Test
    @DisplayName("Поиск методом findById не нашел автора")
    void findById_authorNotFound() {
        Optional<Author> author = authorDao.findById(WRONG_AUTHOR_ID);
        assertThat(author).isEmpty();
    }

    @Test
    @DisplayName("Успешное добавление автора")
    void save_ok() {
        Author authorWithId = authorDao.save(newAuthor);
        Author fromDb = mongoTemplate.findById(authorWithId.getId(), Author.class);
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getFirstName()).isEqualTo(newAuthor.getFirstName());
        assertThat(fromDb.getSurname()).isEqualTo(newAuthor.getSurname());
    }

    @Test
    @DisplayName("Успешное получение списка всех авторов")
    void getAll_ok() {
        mongoTemplate.save(newAuthor);
        Collection<Author> authors = authorDao.findAll();
        assertThat(authors).containsExactlyInAnyOrderElementsOf(List.of(tolstoyAuthor, newAuthor));
    }

    @Test
    @DisplayName("Успешный поиск по имени и фамилии")
    void findByNameAndSurname_ok() {
        Optional<Author> author = authorDao.findByFirstNameAndSurname(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME);
        assertThat(author).isPresent();
        assertThat(author.get()).isEqualTo(tolstoyAuthor);
    }

    @Test
    @DisplayName("Поиск по имени и фамилии не нашел автора")
    void findByNameAndSurname_authorNotFound() {
        Optional<Author> author = authorDao.findByFirstNameAndSurname(DOSTOEVSKY_FIRST_NAME, DOSTOEVSKY_SURNAME);
        assertThat(author).isEmpty();
    }
}