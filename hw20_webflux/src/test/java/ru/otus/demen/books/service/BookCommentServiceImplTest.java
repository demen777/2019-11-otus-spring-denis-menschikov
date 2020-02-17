package ru.otus.demen.books.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.TransientDataAccessResourceException;
import reactor.core.publisher.Mono;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.exception.DataAccessServiceException;
import ru.otus.demen.books.service.exception.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = ServiceTestConfiguration.class)
class BookCommentServiceImplTest {
    private static final String TOLSTOY_AUTHOR_ID = "1";
    private static final String TOLSTOY_FIRST_NAME = "Лев";
    private static final String TOLSTOY_SURNAME = "Толстой";
    private static final String NOVEL_GENRE_NAME = "Роман";
    private static final String NOVEL_GENRE_ID = "1";
    private static final String WAR_AND_PEACE_ID = "1";
    private static final String COMMENT_TEXT = "Хорошая книга";
    private static final String WAR_AND_PEACE_COMMENT_ID = "1";

    private BookComment warAndPeaceComment;

    @Autowired
    BookCommentServiceImpl bookCommentService;

    @Autowired
    BookDao bookDao;

    @BeforeEach
    void setUp() {
        Author tolstoyAuthor = new Author(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME);
        tolstoyAuthor.setId(TOLSTOY_AUTHOR_ID);
        Genre novelGenre = new Genre(NOVEL_GENRE_NAME);
        novelGenre.setId(NOVEL_GENRE_ID);
        warAndPeaceComment = new BookComment(COMMENT_TEXT);
        warAndPeaceComment.setId(WAR_AND_PEACE_COMMENT_ID);
    }

    @Test
    @DisplayName("Успешное добавление комментария")
    void add_ok() {
        when(bookDao.countById(WAR_AND_PEACE_ID)).thenReturn(Mono.just(1L));
        BookComment bookComment = bookCommentService.add(WAR_AND_PEACE_ID, COMMENT_TEXT).block();
        assertThat(bookComment.getText()).isEqualTo(warAndPeaceComment.getText());
    }

    @Test
    @DisplayName("При добавлении комментария с неверным id книги выбрасывается NotFoundException")
    void add_bookNotFound() {
        when(bookDao.findById("-1")).thenReturn(Mono.empty());
        assertThatThrownBy(() -> bookCommentService.add("-1", "Комментарий"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("При добавлении комментария происходит ошибка на уровне Dao -> выбрасывается DataAccessServiceException")
    void add_dataAccessServiceException() {
        when(bookDao.countById(WAR_AND_PEACE_ID))
                .thenThrow(new TransientDataAccessResourceException("TransientDataAccessResourceException"));
        assertThatThrownBy(() -> bookCommentService.add(WAR_AND_PEACE_ID, "Комментарий"))
                .isInstanceOf(DataAccessServiceException.class);
    }

    @Test
    @DisplayName("Успешное удаление комментария")
    void deleteById_ok() {
        when(bookDao.removeCommentById(WAR_AND_PEACE_COMMENT_ID)).thenReturn(Mono.just(1L));
        assertThat(bookCommentService.deleteById(WAR_AND_PEACE_COMMENT_ID).block()).isTrue();
    }

    @Test
    @DisplayName("Удаление комментария отсуствующего в хранилище")
    void deleteById_idNotFound() {
        when(bookDao.removeCommentById(WAR_AND_PEACE_COMMENT_ID)).thenReturn(Mono.just(0L));
        assertThat(bookCommentService.deleteById(WAR_AND_PEACE_COMMENT_ID).block()).isFalse();
    }
}