package ru.otus.demen.books.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = BookServiceImpl.class)
class BookServiceImplTest {
    private static final long TOLSTOY_AUTHOR_ID = 1L;
    private static final Author TOLSTOY_AUTHOR = new Author(TOLSTOY_AUTHOR_ID, "Лев", "Толстой");
    private static final String NOVEL_GENRE_NAME = "Роман";
    private static final long NOVEL_GENRE_ID = 1L;
    private static final Genre NOVEL_GENRE = new Genre(NOVEL_GENRE_ID, NOVEL_GENRE_NAME);
    private static final long WAR_AND_PEACE_ID = 1L;
    private static final String WAR_AND_PEACE_NAME = "Война и мир";

    @Autowired
    BookService bookService;

    @MockBean
    AuthorService authorService;

    @MockBean
    GenreService genreService;

    @MockBean
    BookDao bookDao;

    @Test
    @DisplayName("Успешное добавление книги")
    void addBook_ok() throws ServiceError {
        when(authorService.getById(TOLSTOY_AUTHOR_ID)).thenReturn(TOLSTOY_AUTHOR);
        when(genreService.getByName(NOVEL_GENRE_NAME)).thenReturn(NOVEL_GENRE);
        Book book = new Book(WAR_AND_PEACE_NAME, TOLSTOY_AUTHOR, NOVEL_GENRE);
        Book bookWithId = new Book(WAR_AND_PEACE_ID, WAR_AND_PEACE_NAME, TOLSTOY_AUTHOR, NOVEL_GENRE);
        when(bookDao.save(book)).thenReturn(bookWithId);
        Book bookFromService = bookService.addBook(WAR_AND_PEACE_NAME, TOLSTOY_AUTHOR_ID, NOVEL_GENRE_NAME);
        assertThat(bookFromService).isEqualTo(bookWithId);
    }

    @Test
    @DisplayName("При добавлении книги произошло DataAccessException исключение в BookDao")
    void addBook_bookDaoThrowDataAccessException() throws ServiceError {
        when(authorService.getById(TOLSTOY_AUTHOR_ID)).thenReturn(TOLSTOY_AUTHOR);
        when(genreService.getByName(NOVEL_GENRE_NAME)).thenReturn(NOVEL_GENRE);
        Book book = new Book(WAR_AND_PEACE_NAME, TOLSTOY_AUTHOR, NOVEL_GENRE);
        when(bookDao.save(book)).thenThrow(new DataIntegrityViolationException("DataIntegrityViolationException!!!"));
        assertThatThrownBy(() -> bookService.addBook(WAR_AND_PEACE_NAME, TOLSTOY_AUTHOR_ID, NOVEL_GENRE_NAME))
                .isInstanceOf(ServiceError.class).hasMessageContaining("Ошибка Dao во время добавления книги");
    }
}