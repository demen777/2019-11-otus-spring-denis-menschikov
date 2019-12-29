package ru.otus.demen.books.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ComponentScan(basePackages = "ru.otus.demen.books.dao")
class BookDaoJpaTest {
    private static final long TOLSTOY_AUTHOR_ID = 1L;
    private static final String TOLSTOY_FIRST_NAME = "Лев";
    private static final String TOLSTOY_SURNAME = "Толстой";
    private static final String NOVEL_GENRE_NAME = "Роман";
    private static final long NOVEL_GENRE_ID = 1L;
    private static final long WAR_AND_PEACE_ID = 1L;
    private static final String WAR_AND_PEACE_NAME = "Война и мир";
    private static final long ANNA_KARENINA_ID = 2L;
    private static final String ANNA_KARENINA_NAME = "Анна Каренина";

    private Author tolstoyAuthor;
    private Genre novelGenre;
    private Book warAndPeaceWithId;

    @Autowired
    BookDao bookDao;

    @Autowired
    private TestEntityManager em;

    @BeforeEach
    void setUp() {
        tolstoyAuthor = new Author(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME);
        tolstoyAuthor.setId(TOLSTOY_AUTHOR_ID);
        novelGenre = new Genre(NOVEL_GENRE_NAME);
        novelGenre.setId(NOVEL_GENRE_ID);
        warAndPeaceWithId =
                new Book(WAR_AND_PEACE_NAME, tolstoyAuthor, novelGenre);
        warAndPeaceWithId.setId(WAR_AND_PEACE_ID);
    }

    @Test
    @DisplayName("Успешное добавление новой книги")
    void save_ok() {
        Book book = bookDao.save(new Book(ANNA_KARENINA_NAME, tolstoyAuthor, novelGenre));
        em.clear();
        Book bookFromDb = em.find(Book.class, book.getId());
        assertThat(bookFromDb).isNotNull();
        assertThat(bookFromDb).isEqualTo(book);
    }

    @Test
    @DisplayName("Поиск по фамилии возвращает список книг")
    void findBySurname_ok() {
        Collection<Book> books = bookDao.findBySurname(TOLSTOY_SURNAME);
        assertThat(books).containsExactlyInAnyOrderElementsOf(List.of(warAndPeaceWithId));
    }

    @Test
    @DisplayName("Поиск по id возратил книгу")
    void findById_ok() {
        Optional<Book> book = bookDao.findById(TOLSTOY_AUTHOR_ID);
        assertThat(book.isPresent()).isTrue();
        assertThat(book.get()).isEqualTo(warAndPeaceWithId);
    }

    @Test
    @DisplayName("Поиск по id не нашел книгу")
    void findById_notFound() {
        Optional<Book> book = bookDao.findById(ANNA_KARENINA_ID);
        assertThat(book.isPresent()).isFalse();
    }
}