package ru.otus.demen.books.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.demen.books.dao.AuthorDao;
import ru.otus.demen.books.dao.BookCommentDao;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.dao.GenreDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.BookCommentService;
import ru.otus.demen.books.service.BookService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext
class BookCommentServiceImplSecurityTest {
    private final static Author TOLSTOY = new Author(1L, "Лев", "Толстой");
    private final static Genre NOVEL = new Genre(1L, "Роман");
    private final static Book WAR_AND_PEACE = new Book(1L, "Война и мир", TOLSTOY, NOVEL);
    private final static BookComment WAR_AND_PEACE_COMMENT
            = new BookComment(1L, "Большая книга", WAR_AND_PEACE);
    private final static Book ANNA_KARENINA = new Book(2L, "Анна Каренина", TOLSTOY, NOVEL);

    @MockBean
    private BookDao bookDao;

    @MockBean
    private BookCommentDao bookCommentDao;

    @Autowired
    BookCommentService bookCommentService;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void add_non_special_by_user() {
        when(bookDao.findById(WAR_AND_PEACE.getId())).thenReturn(Optional.of(WAR_AND_PEACE));
        when(bookCommentDao.save(any(BookComment.class))).thenReturn(WAR_AND_PEACE_COMMENT);
        assertThat(bookCommentService.add(WAR_AND_PEACE.getId(), WAR_AND_PEACE_COMMENT.getText()))
                .isEqualTo(WAR_AND_PEACE_COMMENT);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void add_special_by_user() {
        assertThatThrownBy(() -> bookCommentService.add(ANNA_KARENINA.getId(), WAR_AND_PEACE_COMMENT.getText()))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void deleteById_non_special_by_user() {
        when(bookCommentDao.findByBookIdAndId(WAR_AND_PEACE.getId(), WAR_AND_PEACE_COMMENT.getId()))
                .thenReturn(Optional.of(WAR_AND_PEACE_COMMENT));
        when(bookCommentDao.removeById(WAR_AND_PEACE_COMMENT.getId())).thenReturn(1L);
        assertThat(bookCommentService.deleteById(WAR_AND_PEACE.getId(), WAR_AND_PEACE_COMMENT.getId())).isTrue();
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void deleteById_special_by_user() {
        assertThatThrownBy(() -> bookCommentService.deleteById(ANNA_KARENINA.getId(), WAR_AND_PEACE_COMMENT.getId()))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getByBookId_non_special_by_user() {
        when(bookDao.findById(WAR_AND_PEACE.getId())).thenReturn(Optional.of(WAR_AND_PEACE));
        when(bookCommentDao.findByBookId(WAR_AND_PEACE.getId()))
                .thenReturn(List.of(WAR_AND_PEACE_COMMENT));
        assertThat(bookCommentService.getByBookId(WAR_AND_PEACE.getId()))
                .containsExactlyInAnyOrder(WAR_AND_PEACE_COMMENT);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getByBookId_special_by_user() {
        assertThatThrownBy(() -> bookCommentService.getByBookId(ANNA_KARENINA.getId()))
                .isInstanceOf(AccessDeniedException.class);
    }
}