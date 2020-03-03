package ru.otus.demen.books.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import ru.otus.demen.books.dao.AuthorDao;
import ru.otus.demen.books.dao.BookCommentDao;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.dao.GenreDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.BookService;
import ru.otus.demen.books.service.BookServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookServiceImplSecurityTest {
    private final static Author TOLSTOY = new Author(1L, "Лев", "Толстой");
    private final static Genre NOVEL = new Genre(1L, "Роман");
    private final static Book WAR_AND_PEACE = new Book(1L, "Война и мир", TOLSTOY, NOVEL);
    private final static Book ANNA_KARENINA = new Book(2L, "Анна Каренина", TOLSTOY, NOVEL);

    @MockBean
    private BookDao bookDao;

    @MockBean
    private BookCommentDao bookCommentDao;

    @MockBean
    private AuthorDao authorDao;

    @MockBean
    private GenreDao genreDao;

    @Autowired
    BookService bookService;

    @Test
    @WithMockUser(username = "user", roles = "ROLE_USER")
    void findBySurname_without_special() {
        when(bookDao.findByAuthorSurname(TOLSTOY.getSurname()))
            .thenReturn(new ArrayList<>(Arrays.asList(WAR_AND_PEACE, ANNA_KARENINA)));
        assertThat(bookService.findBySurname(TOLSTOY.getSurname())).containsExactly(WAR_AND_PEACE);
    }

    @Test
    void getById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }
}