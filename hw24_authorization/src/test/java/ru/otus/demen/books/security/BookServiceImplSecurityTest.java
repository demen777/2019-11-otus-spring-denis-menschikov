package ru.otus.demen.books.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import ru.otus.demen.books.dao.AuthorDao;
import ru.otus.demen.books.dao.BookCommentDao;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.dao.GenreDao;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.BookService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = AclSecurityTestConfiguration.class)
class BookServiceImplSecurityTest {
    private final static Author TOLSTOY = new Author(1L, "Лев", "Толстой");
    private final static Genre NOVEL = new Genre(1L, "Роман");
    private final static Book WAR_AND_PEACE = new Book(1L, "Война и мир", TOLSTOY, NOVEL);
    private final static Book ANNA_KARENINA = new Book(2L, "Анна Каренина", TOLSTOY, NOVEL);

    @Autowired
    private BookDao bookDao;

    @Autowired
    private BookCommentDao bookCommentDao;

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private GenreDao genreDao;

    @Autowired
    BookService bookService;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Фильтрация специальных книг при получении из по фамилии автора с ролью USER")
    void findBySurname_by_user() {
        when(bookDao.findByAuthorSurname(TOLSTOY.getSurname()))
                .thenReturn(new ArrayList<>(Arrays.asList(WAR_AND_PEACE, ANNA_KARENINA)));
        assertThat(bookService.findBySurname(TOLSTOY.getSurname())).containsExactly(WAR_AND_PEACE);
    }

    @Test
    @WithMockUser(username = "operator", roles = "OPERATOR")
    @DisplayName("Отсутсвие фильтрации специальных книг при получении из по фамилии автора с ролью OPERATOR")
    void findBySurname_by_operator() {
        when(bookDao.findByAuthorSurname(TOLSTOY.getSurname()))
                .thenReturn(new ArrayList<>(Arrays.asList(WAR_AND_PEACE, ANNA_KARENINA)));
        assertThat(bookService.findBySurname(TOLSTOY.getSurname()))
                .containsExactlyInAnyOrder(WAR_AND_PEACE, ANNA_KARENINA);
    }

    @Test
    @WithMockUser(username = "special", roles = {"USER", "SPECIAL_BOOK"})
    @DisplayName("Отсутсвие фильтрации специальных книг при получении из по фамилии автора с ролью SPECIAL_BOOK")
    void findBySurname_by_special() {
        when(bookDao.findByAuthorSurname(TOLSTOY.getSurname()))
                .thenReturn(new ArrayList<>(Arrays.asList(WAR_AND_PEACE, ANNA_KARENINA)));
        assertThat(bookService.findBySurname(TOLSTOY.getSurname()))
                .containsExactlyInAnyOrder(WAR_AND_PEACE, ANNA_KARENINA);
    }


    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Отказ в доступе при попытке получить специальную книгу по id ролью USER")
    void getById_special_by_user() {
        assertThatThrownBy(() -> bookService.getById(ANNA_KARENINA.getId())).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(username = "special", roles = {"USER", "SPECIAL_BOOK"})
    @DisplayName("Успешное получение специальной книги по id ролью SPECIAL_BOOK")
    void getById_special_by_special() {
        when(bookDao.findById(ANNA_KARENINA.getId())).thenReturn(Optional.of(ANNA_KARENINA));
        assertThat(bookService.getById(ANNA_KARENINA.getId())).isEqualTo(ANNA_KARENINA);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Успешное получение специальной книги по id ролью ADMIN")
    void getById_special_by_admin() {
        when(bookDao.findById(ANNA_KARENINA.getId())).thenReturn(Optional.of(ANNA_KARENINA));
        assertThat(bookService.getById(ANNA_KARENINA.getId())).isEqualTo(ANNA_KARENINA);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Успешное получение не специальной книги по id ролью USER")
    void getById_non_special_by_user() {
        when(bookDao.findById(WAR_AND_PEACE.getId())).thenReturn(Optional.of(WAR_AND_PEACE));
        assertThat(bookService.getById(WAR_AND_PEACE.getId())).isEqualTo(WAR_AND_PEACE);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Фильтрация специальных книг при получении списка всех книг с ролью USER")
    void findAll_by_user() {
        when(bookDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(WAR_AND_PEACE, ANNA_KARENINA)));
        assertThat(bookService.findAll()).containsExactlyInAnyOrder(WAR_AND_PEACE);
    }

    @Test
    @WithMockUser(username = "special", roles = {"USER", "SPECIAL_BOOK"})
    @DisplayName("Отсутствие фильтрации специальных книг при получении списка всех книг с ролью SPECIAL_BOOK")
    void findAll_by_special() {
        when(bookDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(WAR_AND_PEACE, ANNA_KARENINA)));
        assertThat(bookService.findAll()).containsExactlyInAnyOrder(WAR_AND_PEACE, ANNA_KARENINA);
    }


    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Успешное изменение не специальной книги ролью USER")
    void update_non_special_by_user() {
        when(bookDao.findById(WAR_AND_PEACE.getId())).thenReturn(Optional.of(WAR_AND_PEACE));
        when(authorDao.findById(TOLSTOY.getId())).thenReturn(Optional.of(TOLSTOY));
        when(genreDao.findById(NOVEL.getId())).thenReturn(Optional.of(NOVEL));
        when(bookDao.save(WAR_AND_PEACE)).thenReturn(WAR_AND_PEACE);
        bookService.update(WAR_AND_PEACE.getId(), WAR_AND_PEACE.getName(), TOLSTOY.getId(), NOVEL.getId());
        verify(bookDao, times(1)).save(WAR_AND_PEACE);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Отказ в доступе при попытке изменить специальную книгу ролью USER")
    void update_special_by_user() {
        assertThatThrownBy(() ->
                bookService.update(ANNA_KARENINA.getId(), ANNA_KARENINA.getName(), TOLSTOY.getId(), NOVEL.getId()))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(username = "special", roles = {"USER", "SPECIAL_BOOK"})
    @DisplayName("Успешное изменение специальной книги ролью SPECIAL_BOOK")
    void update_special_by_special() {
        when(bookDao.findById(ANNA_KARENINA.getId())).thenReturn(Optional.of(ANNA_KARENINA));
        when(authorDao.findById(TOLSTOY.getId())).thenReturn(Optional.of(TOLSTOY));
        when(genreDao.findById(NOVEL.getId())).thenReturn(Optional.of(NOVEL));
        when(bookDao.save(ANNA_KARENINA)).thenReturn(ANNA_KARENINA);
        bookService.update(ANNA_KARENINA.getId(), ANNA_KARENINA.getName(), TOLSTOY.getId(), NOVEL.getId());
        verify(bookDao, times(1)).save(ANNA_KARENINA);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Успешное удаление не специальной книги ролью USER")
    void deleteById_non_special_by_user() {
        bookService.deleteById(WAR_AND_PEACE.getId());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Отказ в доступе при попытке удалить специальную книгу ролью USER")
    void deleteById_special_by_user() {
        assertThatThrownBy(() -> bookService.deleteById(ANNA_KARENINA.getId()))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(username = "special", roles = {"USER", "SPECIAL_BOOK"})
    @DisplayName("Успешное удаление специальной книги ролью SPECIAL_BOOK")
    void deleteById_special_by_special() {
        bookService.deleteById(ANNA_KARENINA.getId());
    }
}