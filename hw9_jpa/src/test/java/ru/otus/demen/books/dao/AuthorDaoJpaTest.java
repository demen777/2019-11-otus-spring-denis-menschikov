package ru.otus.demen.books.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
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
    private static final Author TOLSTOY_AUTHOR = new Author(TOLSTOY_AUTHOR_ID, TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME);
    private static final long WRONG_AUTHOR_ID = -1L;
    private static final String DOSTOEVSKY_FIRST_NAME = "Федор";
    private static final String DOSTOEVSKY_SURNAME = "Достоевский";
    private static final Author NEW_AUTHOR = new Author(DOSTOEVSKY_FIRST_NAME, DOSTOEVSKY_SURNAME);

    @Autowired
    AuthorDao authorDao;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Успешный поиск методом findById")
    void findById_ok() {
        Optional<Author> author = authorDao.findById(TOLSTOY_AUTHOR_ID);
        assertThat(author).isPresent().get().isEqualTo(TOLSTOY_AUTHOR);
    }

    @Test
    @DisplayName("Поиск методом findById не нашел автора")
    void findById_authorNotFound() {
        Optional<Author> author = authorDao.findById(WRONG_AUTHOR_ID);
        assertThat(author.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Успешное добавление автора")
    void save_ok() {
        Author newAuthor = NEW_AUTHOR;
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
        em.persist(NEW_AUTHOR);
        em.clear();
        Collection<Author> authors = authorDao.getAll();
        assertThat(authors).containsExactlyInAnyOrderElementsOf(List.of(TOLSTOY_AUTHOR, NEW_AUTHOR));
    }

    @Test
    @DisplayName("Успешный поиск по имени и фамилии")
    void findByNameAndSurname_ok() {
        Optional<Author> author = authorDao.findByNameAndSurname(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME);
        assertThat(author.isPresent()).isTrue();
        assertThat(author.get()).isEqualTo(TOLSTOY_AUTHOR);
    }

    @Test
    @DisplayName("Поиск по имени и фамилии не нашел автора")
    void findByNameAndSurname_authorNotFound() {
        Optional<Author> author = authorDao.findByNameAndSurname(DOSTOEVSKY_FIRST_NAME, DOSTOEVSKY_SURNAME);
        assertThat(author.isPresent()).isFalse();
    }
}