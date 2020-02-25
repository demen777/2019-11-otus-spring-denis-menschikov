package ru.otus.demen.books.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessResourceFailureException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.demen.books.dao.AuthorDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.service.exception.AlreadyExistsException;
import ru.otus.demen.books.service.exception.DataAccessServiceException;
import ru.otus.demen.books.service.exception.NotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = ServiceTestConfiguration.class)
class AuthorServiceImplTest {
    private static final String TOLSTOY_AUTHOR_ID = "1";
    private static final String TOLSTOY_FIRST_NAME = "Лев";
    private static final String TOLSTOY_SURNAME = "Толстой";
    private static final String WRONG_AUTHOR_ID = "-1";
    private static final String ERR_MSG_AUTHOR_ID_NOT_FOUND = String.format("Не найден автор с id=%s", WRONG_AUTHOR_ID);
    private static final String ERR_MSG_DAO_ERROR = "Ошибка Dao";
    private static final String ERR_MSG_AUTHOR_ALREADY_EXISTS
            = String.format("Автор %s %s уже существует в БД", TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME);

    private Author tolstoyAuthorWithoutId;
    private Author tolstoyAuthor;

    @Autowired
    AuthorService authorService;

    @Autowired
    AuthorDao authorDao;

    @BeforeEach
    void setUp() {
        tolstoyAuthorWithoutId = new Author(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME);
        tolstoyAuthor = new Author(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME);
        tolstoyAuthor.setId(TOLSTOY_AUTHOR_ID);
    }

    @Test
    @DisplayName("Успешный поиск методом findById")
    void findById_ok() {
        when(authorDao.findById(TOLSTOY_AUTHOR_ID)).thenReturn(Mono.just(tolstoyAuthor));
        Optional<Author> author = authorService.findById(TOLSTOY_AUTHOR_ID).blockOptional();
        //noinspection OptionalGetWithoutIsPresent
        assertThat(author.get()).isEqualTo(tolstoyAuthor);
    }

    @Test
    @DisplayName("Поиск методом findById не нашел автора")
    void findById_authorNotFoundById() {
        when(authorDao.findById(WRONG_AUTHOR_ID)).thenReturn(Mono.empty());
        Optional<Author> author = authorService.findById(WRONG_AUTHOR_ID).blockOptional();
        assertThat(author.isPresent()).isFalse();
    }

    @Test
    @DisplayName("При поиске автора произошло DataAccessException исключение в AuthorDao")
    void findById_authorDaoThrowDataAccessException() {
        when(authorDao.findById(TOLSTOY_AUTHOR_ID))
                .thenReturn(Mono.error(
                        new DataAccessResourceFailureException("DataAccessResourceFailureException!!!")));
        assertThatThrownBy(() -> authorService.findById(TOLSTOY_AUTHOR_ID).block())
                .isInstanceOf(DataAccessServiceException.class).hasMessageStartingWith(ERR_MSG_DAO_ERROR);
    }

    @Test
    @DisplayName("Успешное получение автора методом getById")
    void getById_ok() {
        when(authorDao.findById(TOLSTOY_AUTHOR_ID)).thenReturn(Mono.just(tolstoyAuthor));
        Author author = authorService.getById(TOLSTOY_AUTHOR_ID).block();
        assertThat(author).isEqualTo(tolstoyAuthor);
    }

    @Test
    @DisplayName("Получение автора методом getById выбросило исключение ServiceError ввиду отсуствия автора")
    void getById_authorNotFoundById() {
        when(authorDao.findById(WRONG_AUTHOR_ID)).thenReturn(Mono.empty());
        assertThatThrownBy(() -> authorService.getById(WRONG_AUTHOR_ID).block())
                .isInstanceOf(NotFoundException.class).hasMessageContaining(ERR_MSG_AUTHOR_ID_NOT_FOUND);
    }

    @Test
    @DisplayName("Успешное добавление автора")
    void add_ok() {
        when(authorDao.save(new Author(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME))).thenReturn(Mono.just(tolstoyAuthor));
        when(authorDao.findByFirstNameAndSurname(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME)).thenReturn(Mono.empty());
        Author author = authorService.add(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME).block();
        assertThat(author).isEqualTo(tolstoyAuthor);
    }

    @Test
    @DisplayName("Попытка добавить уже существующего в БД автора")
    void add_alreadyExists() {
        when(authorDao.save(new Author(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME))).thenReturn(Mono.just(tolstoyAuthor));
        when(authorDao.findByFirstNameAndSurname(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME))
                .thenReturn(Mono.just(tolstoyAuthor));
        assertThatThrownBy(() -> authorService.add(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME).block())
                .isInstanceOf(AlreadyExistsException.class).hasMessageContaining(ERR_MSG_AUTHOR_ALREADY_EXISTS);
    }

    @Test
    @DisplayName("При добавлении автора произошло DataAccessException исключение в AuthorDao")
    void add_authorDaoThrowDataAccessException() {
        when(authorDao.findByFirstNameAndSurname(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME)).thenReturn(Mono.empty());
        when(authorDao.save(tolstoyAuthorWithoutId))
                .thenReturn(Mono.error(
                        new DataAccessResourceFailureException("DataAccessResourceFailureException!!!")));
        assertThatThrownBy(() -> authorService.add(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME).block())
                .isInstanceOf(DataAccessServiceException.class).hasMessageStartingWith(ERR_MSG_DAO_ERROR);
    }

    @Test
    @DisplayName("Успешный поиск по имени и фамилии")
    void findByNameAndSurname_ok() {
        when(authorDao.findByFirstNameAndSurname(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME)).thenReturn(Mono.just(tolstoyAuthor));
        Optional<Author> author = authorService.findByNameAndSurname(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME).blockOptional();
        //noinspection OptionalGetWithoutIsPresent
        assertThat(author.get()).isEqualTo(tolstoyAuthor);
    }

    @Test
    @DisplayName("Поиск по имени и фамилии не нашел автора")
    void findByNameAndSurname_authorNotExist() {
        when(authorDao.findByFirstNameAndSurname(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME)).thenReturn(Mono.empty());
        Optional<Author> author = authorService.findByNameAndSurname(TOLSTOY_FIRST_NAME, TOLSTOY_SURNAME).blockOptional();
        assertThat(author.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Успешное получение списка всех авторов")
    void getAll_ok() {
        when(authorDao.findAll()).thenReturn(Flux.just(tolstoyAuthor));
        Collection<Author> authors = authorService.getAll().collectList().block();
        assertThat(authors).containsExactlyInAnyOrderElementsOf(List.of(tolstoyAuthor));
    }
}