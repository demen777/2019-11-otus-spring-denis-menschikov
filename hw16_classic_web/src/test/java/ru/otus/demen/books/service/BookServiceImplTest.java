package ru.otus.demen.books.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import ru.otus.demen.books.dao.AuthorDao;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.dao.GenreDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.exception.DataAccessServiceException;
import ru.otus.demen.books.service.exception.IllegalParameterException;
import ru.otus.demen.books.service.exception.NotFoundException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = ServiceTestConfiguration.class)
class BookServiceImplTest {
    private static final String ERR_MSG_DAO_ERROR = "Ошибка Dao";
    private static final long TOLSTOY_AUTHOR_ID = 1L;
    private static final String TOLSTOY_FIRST_NAME = "Лев";
    private static final String TOLSTOY_SURNAME = "Толстой";
    private static final String NOVEL_GENRE_NAME = "Роман";
    private static final long NOVEL_GENRE_ID = 1L;
    private static final long WAR_AND_PEACE_ID = 1L;
    private static final String WAR_AND_PEACE_NAME = "Война и мир";
    private static final long ANNA_KARENINA_ID = 2L;
    private static final String ANNA_KARENINA_NAME = "Анна Каренина";

    private Book annaKareninaWithId;
    private Author tolstoyAuthor;
    private Genre novelGenre;
    private Book warAndPeaceWithId;

    @Autowired
    BookService bookService;

    @Autowired
    AuthorDao authorDao;

    @Autowired
    GenreDao genreDao;

    @Autowired
    BookDao bookDao;

    @BeforeEach
    void setUp() {
        tolstoyAuthor = new Author(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME);
        tolstoyAuthor.setId(TOLSTOY_AUTHOR_ID);
        novelGenre = new Genre(NOVEL_GENRE_NAME);
        novelGenre.setId(NOVEL_GENRE_ID);
        warAndPeaceWithId =
                new Book(WAR_AND_PEACE_NAME, tolstoyAuthor, novelGenre);
        warAndPeaceWithId.setId(WAR_AND_PEACE_ID);
        annaKareninaWithId = new Book(ANNA_KARENINA_NAME, tolstoyAuthor, novelGenre);
        annaKareninaWithId.setId(ANNA_KARENINA_ID);
    }

    @Test
    @DisplayName("Успешное получение книги по id")
    void getById_ok() {
        when(bookDao.findById(WAR_AND_PEACE_ID)).thenReturn(Optional.of(warAndPeaceWithId));
        Book book = bookService.getById(WAR_AND_PEACE_ID);
        assertThat(book).isEqualTo(warAndPeaceWithId);
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
        when(authorDao.findById(TOLSTOY_AUTHOR_ID)).thenReturn(Optional.of(tolstoyAuthor));
        when(genreDao.findById(NOVEL_GENRE_ID)).thenReturn(Optional.of(novelGenre));
        Book book = new Book(WAR_AND_PEACE_NAME, tolstoyAuthor, novelGenre);
        when(bookDao.save(book)).thenReturn(warAndPeaceWithId);
        Book bookFromService = bookService.add(WAR_AND_PEACE_NAME, TOLSTOY_AUTHOR_ID, NOVEL_GENRE_ID);
        assertThat(bookFromService).isEqualTo(warAndPeaceWithId);
    }

    @Test
    @DisplayName("При добавлении книги с пустым именем выбрасывается исключение")
    void add_emptyName() {
        assertThatThrownBy(() -> bookService.add("", TOLSTOY_AUTHOR_ID, NOVEL_GENRE_ID))
                .isInstanceOf(IllegalParameterException.class).hasMessageStartingWith("Имя книги должно быть не пустым");
    }

    @Test
    @DisplayName("При добавлении книги произошло DataAccessException исключение в BookDao")
    void add_bookDaoThrowDataAccessException() {
        when(authorDao.findById(TOLSTOY_AUTHOR_ID)).thenReturn(Optional.of(tolstoyAuthor));
        when(genreDao.findById(NOVEL_GENRE_ID)).thenReturn(Optional.of(novelGenre));
        Book book = new Book(WAR_AND_PEACE_NAME, tolstoyAuthor, novelGenre);
        when(bookDao.save(book)).thenThrow(new DataIntegrityViolationException("DataIntegrityViolationException!!!"));
        assertThatThrownBy(() -> bookService.add(WAR_AND_PEACE_NAME, TOLSTOY_AUTHOR_ID, NOVEL_GENRE_ID))
                .isInstanceOf(DataAccessServiceException.class).hasMessageContaining(ERR_MSG_DAO_ERROR);
    }

    @Test
    @DisplayName("Успешное получение списка книг по фамилии автора")
    void findBySurname_ok() {
        List<Book> expectedBooks = Arrays.asList(warAndPeaceWithId, annaKareninaWithId);
        when(bookDao.findByAuthorSurname(TOLSTOY_SURNAME)).thenReturn(expectedBooks);
        List<Book> books = bookService.findBySurname(TOLSTOY_SURNAME);
        assertThat(books).containsExactlyInAnyOrderElementsOf(expectedBooks);
    }

    @Test
    @DisplayName("Получение пустого списка книг по фамилии автора")
    void findBySurname_emptyList() {
        List<Book> expectedBooks = Collections.emptyList();
        when(bookDao.findByAuthorSurname(TOLSTOY_SURNAME)).thenReturn(expectedBooks);
        List<Book> books = bookService.findBySurname(TOLSTOY_SURNAME);
        assertThat(books).containsExactlyInAnyOrderElementsOf(expectedBooks);
    }

    @Test
    @DisplayName("При поиске книг по фамилии произошло DataAccessException исключение в BookDao")
    void findBySurname_bookDaoThrowDataAccessException() {
        when(bookDao.findByAuthorSurname(TOLSTOY_SURNAME))
                .thenThrow(new DataAccessResourceFailureException("DataAccessResourceFailureException!!!"));
        assertThatThrownBy(() -> bookService.findBySurname(TOLSTOY_SURNAME))
                .isInstanceOf(DataAccessServiceException.class).hasMessageContaining(ERR_MSG_DAO_ERROR);
    }
}