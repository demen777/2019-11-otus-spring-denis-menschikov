package ru.otus.demen.books.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.TransientDataAccessResourceException;
import ru.otus.demen.books.dao.AuthorDao;
import ru.otus.demen.books.dao.BookCommentDao;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.exception.DataAccessServiceException;
import ru.otus.demen.books.service.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BookCommentServiceImpl.class)
class BookCommentServiceImplTest {
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
    private static final long WAR_AND_PEACE_COMMENT_ID = 1L;
    private static final BookComment WAR_AND_PEACE_COMMENT
            = new BookComment(WAR_AND_PEACE_COMMENT_ID, COMMENT_TEXT, WAR_AND_PEACE_WITH_ID);

    @Autowired
    BookCommentServiceImpl bookCommentService;

    @MockBean
    BookCommentDao bookCommentDao;

    @MockBean
    BookDao bookDao;

    @Test
    @DisplayName("Успешное добавление комментария")
    void add_ok() {
        when(bookDao.findById(WAR_AND_PEACE_ID)).thenReturn(Optional.of(WAR_AND_PEACE_WITH_ID));
        when(bookCommentDao.save(new BookComment(0L, COMMENT_TEXT, WAR_AND_PEACE_WITH_ID)))
                .thenReturn(WAR_AND_PEACE_COMMENT);
        BookComment bookComment = bookCommentService.add(WAR_AND_PEACE_ID, COMMENT_TEXT);
        assertThat(bookComment).isEqualTo(WAR_AND_PEACE_COMMENT);
    }

    @Test
    @DisplayName("При добавлении комментария с неверным id книги выбрасывается NotFoundException")
    void add_bookNotFound() {
        when(bookDao.findById(-1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookCommentService.add(-1L, "Комментарий"))
                .isInstanceOf(NotFoundException.class);
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
    @DisplayName("Успешный поиск комментариев по id книги, непустой результат")
    void findByBookId_ok() {
        when(bookCommentDao.findByBookId(WAR_AND_PEACE_ID)).thenReturn(List.of(WAR_AND_PEACE_COMMENT));
        assertThat(bookCommentService.findByBookId(WAR_AND_PEACE_ID))
                .containsExactlyInAnyOrder(WAR_AND_PEACE_COMMENT);
    }

    @Test
    @DisplayName("Успешное удаление комментария")
    void deleteById_ok() {
        when(bookCommentDao.deleteById(WAR_AND_PEACE_COMMENT_ID)).thenReturn(1);
        assertThat(bookCommentService.deleteById(WAR_AND_PEACE_COMMENT_ID)).isTrue();
    }

    @Test
    @DisplayName("Удаление комментария отсуствующего в хранилище")
    void deleteById_idNotFound() {
        when(bookCommentDao.deleteById(WAR_AND_PEACE_COMMENT_ID)).thenReturn(0);
        assertThat(bookCommentService.deleteById(WAR_AND_PEACE_COMMENT_ID)).isFalse();
    }
}