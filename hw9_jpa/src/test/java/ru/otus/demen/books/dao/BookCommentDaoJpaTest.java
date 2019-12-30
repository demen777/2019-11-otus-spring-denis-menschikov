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
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.model.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ComponentScan(basePackages = "ru.otus.demen.books.dao")
class BookCommentDaoJpaTest {
    private static final long TOLSTOY_AUTHOR_ID = 1L;
    private static final String TOLSTOY_AUTHOR_NAME = "Лев";
    private static final String TOLSTOY_AUTHOR_SURNAME = "Толстой";
    private static final String NOVEL_GENRE_NAME = "Роман";
    private static final long NOVEL_GENRE_ID = 1L;
    private static final long WAR_AND_PEACE_ID = 1L;
    private static final String WAR_AND_PEACE_NAME = "Война и мир";
    private static final String COMMENT_TEXT = "Хорошая книга";

    private BookComment warAndPeaceCommentWithoutId;

    @Autowired
    BookCommentDao bookCommentDao;

    @Autowired
    private TestEntityManager em;

    @BeforeEach
    void setUp()
    {
        Author tolstoyAuthor = new Author(TOLSTOY_AUTHOR_NAME, TOLSTOY_AUTHOR_SURNAME);
        tolstoyAuthor.setId(TOLSTOY_AUTHOR_ID);
        Genre novelGenre = new Genre(NOVEL_GENRE_NAME);
        novelGenre.setId(NOVEL_GENRE_ID);
        Book warAndPeaceWithId = new Book(WAR_AND_PEACE_NAME, tolstoyAuthor, novelGenre);
        warAndPeaceWithId.setId(WAR_AND_PEACE_ID);
        warAndPeaceCommentWithoutId = new BookComment(COMMENT_TEXT, warAndPeaceWithId);
    }

    private BookComment addWarAndPeaceComment() {
        em.persist(warAndPeaceCommentWithoutId);
        em.clear();
        return warAndPeaceCommentWithoutId;
    }

    @Test
    @DisplayName("Успешное удаление комментария по id")
    void deleteById_ok() {
        BookComment warAndPeaceComment = addWarAndPeaceComment();
        long bookCommentId = warAndPeaceComment.getId();
        assertThat(bookCommentDao.deleteById(bookCommentId)).isEqualTo(1);
        assertThat(em.find(BookComment.class, bookCommentId)).isNull();
    }

    @Test
    @DisplayName("Комментарий по id не найден")
    void deleteById_notFound() {
        addWarAndPeaceComment();
        assertThat(bookCommentDao.deleteById(-1)).isEqualTo(0);
    }
}