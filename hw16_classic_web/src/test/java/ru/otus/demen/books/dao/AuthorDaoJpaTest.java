package ru.otus.demen.books.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.demen.books.model.Author;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ComponentScan(basePackages = "ru.otus.demen.books.dao")
class AuthorDaoJpaTest {
    private static final long TOLSTOY_AUTHOR_ID = 1L;
    private static final String TOLSTOY_FIRST_NAME = "Лев";
    private static final String TOLSTOY_SURNAME = "Толстой";

    private static final long WRONG_AUTHOR_ID = -1L;
    private static final String CHEKHOV_FIRST_NAME = "Антон";
    private static final String CHEKHOV_SURNAME = "Чехов";

    private final Author newAuthor = new Author(CHEKHOV_FIRST_NAME, CHEKHOV_SURNAME);
    private static final Author TOLSTOY = new Author(TOLSTOY_AUTHOR_ID, TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME);
    private static final Author DOSTOEVSKY = new Author(2L, "Федор", "Достоевский");

    @Autowired
    AuthorDao authorDao;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Успешный поиск методом findById")
    void findById_ok() {
        Optional<Author> author = authorDao.findById(TOLSTOY_AUTHOR_ID);
        assertThat(author).isPresent().get().isEqualTo(TOLSTOY);
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
        em.clear();
        Author fromDb = em.find(Author.class, authorWithId.getId());
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getFirstName()).isEqualTo(newAuthor.getFirstName());
        assertThat(fromDb.getSurname()).isEqualTo(newAuthor.getSurname());
    }

    @Test
    @DisplayName("Успешное получение списка всех авторов")
    void getAll_ok() {
        Collection<Author> authors = authorDao.findAll();
        assertThat(authors).containsExactlyInAnyOrderElementsOf(List.of(TOLSTOY, DOSTOEVSKY));
    }

    @Test
    @DisplayName("Успешный поиск по имени и фамилии")
    void findByNameAndSurname_ok() {
        Optional<Author> author = authorDao.findByFirstNameAndSurname(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME);
        assertThat(author).isPresent();
        assertThat(author.get()).isEqualTo(TOLSTOY);
    }

    @Test
    @DisplayName("Поиск по имени и фамилии не нашел автора")
    void findByNameAndSurname_authorNotFound() {
        Optional<Author> author = authorDao.findByFirstNameAndSurname(CHEKHOV_FIRST_NAME, CHEKHOV_SURNAME);
        assertThat(author).isEmpty();
    }
}