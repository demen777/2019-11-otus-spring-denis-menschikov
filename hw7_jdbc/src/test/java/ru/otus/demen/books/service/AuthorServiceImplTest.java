package ru.otus.demen.books.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import ru.otus.demen.books.dao.AuthorDao;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.dao.GenreDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = AuthorServiceImpl.class)
class AuthorServiceImplTest {
    private static final long TOLSTOY_AUTHOR_ID = 1L;
    private static final Author TOLSTOY_AUTHOR = new Author(TOLSTOY_AUTHOR_ID, "Лев", "Толстой");
    private static final long WRONG_AUTHOR_ID = 2L;
    private static final String ERR_MSG_AUTHOR_ID_NOT_FOUND = String.format("Не найден автор с id=%d", WRONG_AUTHOR_ID);

    @Autowired
    AuthorService authorService;

    @MockBean
    AuthorDao authorDao;

    @Test
    @DisplayName("Успешный поиск методом findById")
    void findById_ok() throws ServiceError {
        when(authorDao.findById(TOLSTOY_AUTHOR_ID)).thenReturn(Optional.of(TOLSTOY_AUTHOR));
        Optional<Author> author = authorService.findById(TOLSTOY_AUTHOR_ID);
        assertThat(author.get()).isEqualTo(TOLSTOY_AUTHOR);
    }

    @Test
    @DisplayName("Поиск методом findById не нашел автора")
    void findById_absentAuthorId() throws ServiceError {
        when(authorDao.findById(WRONG_AUTHOR_ID)).thenReturn(Optional.empty());
        Optional<Author> author = authorService.findById(WRONG_AUTHOR_ID);
        assertThat(author.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Успешное получение автора методом findById")
    void getById_ok() throws ServiceError {
        when(authorDao.findById(TOLSTOY_AUTHOR_ID)).thenReturn(Optional.of(TOLSTOY_AUTHOR));
        Author author = authorService.getById(TOLSTOY_AUTHOR_ID);
        assertThat(author).isEqualTo(TOLSTOY_AUTHOR);
    }

    @Test
    @DisplayName("Получение автора методом findById выбросило исключение ServiceError ввиду отсуствия автора")
    void getById_absentAuthorId() {
        when(authorDao.findById(WRONG_AUTHOR_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> authorService.getById(WRONG_AUTHOR_ID))
                .isInstanceOf(ServiceError.class).hasMessageContaining(ERR_MSG_AUTHOR_ID_NOT_FOUND);
    }

    @Test
    @DisplayName("При поиске автора произошло DataAccessException исключение в AuthorDao")
    void findById_authorDaoThrowDataAccessException() throws ServiceError {
        when(authorDao.findById(TOLSTOY_AUTHOR_ID))
                .thenThrow(new DataAccessResourceFailureException("DataAccessResourceFailureException!!!"));
        assertThatThrownBy(() -> authorService.findById(TOLSTOY_AUTHOR_ID))
                .isInstanceOf(ServiceError.class).hasMessageContaining("Ошибка Dao во время поиска автора по id");
    }
}