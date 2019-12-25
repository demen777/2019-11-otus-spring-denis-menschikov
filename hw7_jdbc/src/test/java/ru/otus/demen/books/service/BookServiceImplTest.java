package ru.otus.demen.books.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.exception.DataAccessServiceException;
import ru.otus.demen.books.service.exception.IllegalParameterException;
import ru.otus.demen.books.service.exception.NotFoundException;
import ru.otus.demen.books.service.exception.ServiceException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = BookServiceImpl.class)
class BookServiceImplTest {
    private static final String ERR_MSG_DAO_ERROR = "Ошибка Dao";
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
    private static final Book ANNA_KARENINA_WITH_ID =
            new Book(ANNA_KARENINA_ID, ANNA_KARENINA_NAME, TOLSTOY_AUTHOR, NOVEL_GENRE);

    @Autowired
    BookService bookService;

    @MockBean
    AuthorService authorService;

    @MockBean
    GenreService genreService;

    @MockBean
    BookDao bookDao;

    @Test
    @DisplayName("Успешное получение книги по id")
    void getById_ok() {
        when(bookDao.findById(WAR_AND_PEACE_ID)).thenReturn(Optional.of(WAR_AND_PEACE_WITH_ID));
        Book book = bookService.getById(WAR_AND_PEACE_ID);
        assertThat(book).isEqualTo(WAR_AND_PEACE_WITH_ID);
    }

    @Test
    @DisplayName("Книга по id не найдена")
    void getById_notFound() {
        when(bookDao.findById(ANNA_KARENINA_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.getById(ANNA_KARENINA_ID)).isInstanceOf(NotFoundException.class)
                .hasMessageStartingWith("Не найдена книга с id=");
    }

    @Test
    @DisplayName("Успешное добавление книги")
    void add_ok() {
        when(authorService.getById(TOLSTOY_AUTHOR_ID)).thenReturn(TOLSTOY_AUTHOR);
        when(genreService.getByName(NOVEL_GENRE_NAME)).thenReturn(NOVEL_GENRE);
        Book book = new Book(WAR_AND_PEACE_NAME, TOLSTOY_AUTHOR, NOVEL_GENRE);
        when(bookDao.save(book)).thenReturn(WAR_AND_PEACE_WITH_ID);
        Book bookFromService = bookService.add(WAR_AND_PEACE_NAME, TOLSTOY_AUTHOR_ID, NOVEL_GENRE_NAME);
        assertThat(bookFromService).isEqualTo(WAR_AND_PEACE_WITH_ID);
    }

    @Test
    @DisplayName("При добавлении книги с пустым именем выбрасывается исключение")
    void add_emptyName() {
        assertThatThrownBy(() -> bookService.add("", TOLSTOY_AUTHOR_ID, NOVEL_GENRE_NAME))
                .isInstanceOf(IllegalParameterException.class).hasMessageStartingWith("Имя книги должно быть не пустым");
    }

    @Test
    @DisplayName("При добавлении книги произошло DataAccessException исключение в BookDao")
    void add_bookDaoThrowDataAccessException() {
        when(authorService.getById(TOLSTOY_AUTHOR_ID)).thenReturn(TOLSTOY_AUTHOR);
        when(genreService.getByName(NOVEL_GENRE_NAME)).thenReturn(NOVEL_GENRE);
        Book book = new Book(WAR_AND_PEACE_NAME, TOLSTOY_AUTHOR, NOVEL_GENRE);
        when(bookDao.save(book)).thenThrow(new DataIntegrityViolationException("DataIntegrityViolationException!!!"));
        assertThatThrownBy(() -> bookService.add(WAR_AND_PEACE_NAME, TOLSTOY_AUTHOR_ID, NOVEL_GENRE_NAME))
                .isInstanceOf(DataAccessServiceException.class).hasMessageContaining(ERR_MSG_DAO_ERROR);
    }

    @Test
    @DisplayName("Успешное получение списка книг по фамилии автора")
    void findBySurname_ok() {
        Collection<Book> expectedBooks = Arrays.asList(WAR_AND_PEACE_WITH_ID, ANNA_KARENINA_WITH_ID);
        when(bookDao.findBySurname(TOLSTOY_AUTHOR_SURNAME)).thenReturn(expectedBooks);
        Collection<Book> books = bookService.findBySurname(TOLSTOY_AUTHOR_SURNAME);
        assertThat(books).containsExactlyInAnyOrderElementsOf(expectedBooks);
    }

    @Test
    @DisplayName("Получение пустого списка книг по фамилии автора")
    void findBySurname_emptyList() {
        Collection<Book> expectedBooks = Collections.emptyList();
        when(bookDao.findBySurname(TOLSTOY_AUTHOR_SURNAME)).thenReturn(expectedBooks);
        Collection<Book> books = bookService.findBySurname(TOLSTOY_AUTHOR_SURNAME);
        assertThat(books).containsExactlyInAnyOrderElementsOf(expectedBooks);
    }

    @Test
    @DisplayName("При поиске книг по фамилии произошло DataAccessException исключение в BookDao")
    void findBySurname_bookDaoThrowDataAccessException() {
        when(bookDao.findBySurname(TOLSTOY_AUTHOR_SURNAME))
                .thenThrow(new DataAccessResourceFailureException("DataAccessResourceFailureException!!!"));
        assertThatThrownBy(() -> bookService.findBySurname(TOLSTOY_AUTHOR_SURNAME))
                .isInstanceOf(DataAccessServiceException.class).hasMessageContaining(ERR_MSG_DAO_ERROR);
    }
}