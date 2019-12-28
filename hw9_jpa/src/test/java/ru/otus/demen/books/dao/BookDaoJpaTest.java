package ru.otus.demen.books.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(BookDaoJpa.class)
class BookDaoJpaTest {
    private static final long TOLSTOY_AUTHOR_ID = 1L;
    private static final String TOLSTOY_AUTHOR_NAME = "Лев";
    private static final String TOLSTOY_AUTHOR_SURNAME = "Толстой";
    private static final Author TOLSTOY_AUTHOR =
            new Author(TOLSTOY_AUTHOR_ID, TOLSTOY_AUTHOR_NAME, TOLSTOY_AUTHOR_SURNAME);
    private static final String NOVEL_GENRE_NAME = "Роман";
    private static final long NOVEL_GENRE_ID = 1L;
    private static final Genre NOVEL_GENRE = new Genre(NOVEL_GENRE_ID, NOVEL_GENRE_NAME);
    private static final long WAR_AND_PEACE_ID = 1L;
    private static final String WAR_AND_PEACE_NAME = "Война и мир";
    private static final Book WAR_AND_PEACE_WITH_ID =
            new Book(WAR_AND_PEACE_ID, WAR_AND_PEACE_NAME, TOLSTOY_AUTHOR, NOVEL_GENRE);
    private static final long ANNA_KARENINA_ID = 2L;
    private static final String ANNA_KARENINA_NAME = "Анна Каренина";

    @Autowired
    BookDao bookDao;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Успешное добавление новой книги")
    void save_ok() {
        Book book = bookDao.save(new Book(ANNA_KARENINA_NAME, TOLSTOY_AUTHOR, NOVEL_GENRE));
        em.clear();
        Book bookFromDb = em.find(Book.class, book.getId());
        assertThat(bookFromDb).isNotNull();
        assertThat(bookFromDb).isEqualTo(book);
    }

    @Test
    @DisplayName("Поиск по фамилии возвращает список книг")
    void findBySurname_ok() {
        Collection<Book> books = bookDao.findBySurname(TOLSTOY_AUTHOR_SURNAME);
        assertThat(books).containsExactlyInAnyOrderElementsOf(List.of(WAR_AND_PEACE_WITH_ID));
    }

    @Test
    @DisplayName("Поиск по id возратил книгу")
    void findById_ok() {
        Optional<Book> book = bookDao.findById(TOLSTOY_AUTHOR_ID);
        assertThat(book.isPresent()).isTrue();
        assertThat(book.get()).isEqualTo(WAR_AND_PEACE_WITH_ID);
    }

    @Test
    @DisplayName("Поиск по id не нашел книгу")
    void findById_notFound() {
        Optional<Book> book = bookDao.findById(ANNA_KARENINA_ID);
        assertThat(book.isPresent()).isFalse();
    }
}