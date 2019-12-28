package ru.otus.demen.books.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.model.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(BookCommentDaoJpa.class)
class BookCommentDaoJpaTest {
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
    private static final String COMMENT_TEXT = "Хорошая книга";

    private BookComment warAndPeaceCommentWithoutId;

    @Autowired
    BookCommentDao bookCommentDao;

    @Autowired
    private TestEntityManager em;

    @BeforeEach
    void setUp()
    {
        warAndPeaceCommentWithoutId = new BookComment(0L, COMMENT_TEXT, WAR_AND_PEACE_WITH_ID);
    }

    @Test
    @DisplayName("Успешное добавление комментария к книги в БД")
    void save_ok() {
        BookComment bookComment = bookCommentDao.save(warAndPeaceCommentWithoutId);
        em.clear();
        BookComment bookCommentFromDb = em.find(BookComment.class, bookComment.getId());
        assertThat(bookCommentFromDb).isNotNull();
        assertThat(bookCommentFromDb.getBook()).isEqualTo(warAndPeaceCommentWithoutId.getBook());
        assertThat(bookCommentFromDb.getText()).isEqualTo(warAndPeaceCommentWithoutId.getText());
    }

    @Test
    @DisplayName("Поиск комментариев по id книги непустой")
    void findByBookId_ok() {
        BookComment bookComment = em.persist(warAndPeaceCommentWithoutId);
        em.clear();
        assertThat(bookCommentDao.findByBookId(WAR_AND_PEACE_ID)).containsExactlyInAnyOrder(bookComment);
    }

    @Test
    @DisplayName("Успешное удаление комментария по id")
    void deleteById_ok() {
        BookComment bookComment = em.persist(warAndPeaceCommentWithoutId);
        em.clear();
        assertThat(bookCommentDao.deleteById(bookComment.getId())).isEqualTo(1);
        assertThat(em.find(BookComment.class, bookComment.getId())).isNull();
    }

    @Test
    @DisplayName("Комментарий по id не найден")
    void deleteById_notFound() {
        BookComment bookComment = em.persist(warAndPeaceCommentWithoutId);
        em.clear();
        assertThat(bookCommentDao.deleteById(-1)).isEqualTo(0);
    }
}