package ru.otus.demen.books.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.security.test.context.support.WithMockUser;
import ru.otus.demen.books.dao.BookCommentDao;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.exception.DataAccessServiceException;
import ru.otus.demen.books.service.exception.IllegalParameterException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = ServiceTestConfiguration.class)
@WithMockUser(roles = "ADMIN")
class BookCommentServiceImplTest {
    private static final long TOLSTOY_AUTHOR_ID = 1L;
    private static final String TOLSTOY_FIRST_NAME = "Лев";
    private static final String TOLSTOY_SURNAME = "Толстой";
    private static final String NOVEL_GENRE_NAME = "Роман";
    private static final long NOVEL_GENRE_ID = 1L;
    private static final long WAR_AND_PEACE_ID = 1L;
    private static final String WAR_AND_PEACE_NAME = "Война и мир";
    private static final String COMMENT_TEXT = "Хорошая книга";
    private static final long WAR_AND_PEACE_COMMENT_ID = 1L;

    private BookComment warAndPeaceComment;
    private Book warAndPeaceWithId;

    @Autowired
    BookCommentServiceImpl bookCommentService;

    @Autowired
    BookCommentDao bookCommentDao;

    @Autowired
    BookDao bookDao;

    @BeforeEach
    void setUp() {
        Author tolstoyAuthor = new Author(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME);
        tolstoyAuthor.setId(TOLSTOY_AUTHOR_ID);
        Genre novelGenre = new Genre(NOVEL_GENRE_NAME);
        novelGenre.setId(NOVEL_GENRE_ID);
        warAndPeaceWithId =
                new Book(WAR_AND_PEACE_NAME, tolstoyAuthor, novelGenre);
        warAndPeaceWithId.setId(WAR_AND_PEACE_ID);
        warAndPeaceComment = new BookComment(COMMENT_TEXT, warAndPeaceWithId);
        warAndPeaceComment.setId(WAR_AND_PEACE_COMMENT_ID);
    }

    @Test
    @DisplayName("Успешное добавление комментария")
    void add_ok() {
        when(bookDao.findById(WAR_AND_PEACE_ID)).thenReturn(Optional.of(warAndPeaceWithId));
        when(bookCommentDao.save(new BookComment(COMMENT_TEXT, warAndPeaceWithId))).thenReturn(warAndPeaceComment);
        BookComment bookComment = bookCommentService.add(WAR_AND_PEACE_ID, COMMENT_TEXT);
        assertThat(bookComment.getText()).isEqualTo(warAndPeaceComment.getText());
    }

    @Test
    @DisplayName("При добавлении комментария происходит ошибка на уровне Dao -> выбрасывается DataAccessServiceException")
    void add_dataAccessServiceException() {
        when(bookDao.findById(WAR_AND_PEACE_ID))
                .thenThrow(new TransientDataAccessResourceException("TransientDataAccessResourceException"));
        assertThatThrownBy(() -> bookCommentService.add(WAR_AND_PEACE_ID, "Комментарий"))
                .isInstanceOf(DataAccessServiceException.class);
    }

    @Test
    @DisplayName("Удаление комментария отсуствующего для книги")
    void deleteById_idNotFound() {
        when(bookCommentDao.removeById(WAR_AND_PEACE_COMMENT_ID)).thenReturn(0L);
        assertThatThrownBy(() -> bookCommentService.deleteById(WAR_AND_PEACE_ID, WAR_AND_PEACE_COMMENT_ID))
                .isInstanceOf(IllegalParameterException.class);
    }
}