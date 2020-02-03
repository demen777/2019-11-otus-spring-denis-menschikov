package ru.otus.demen.books.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.AuthorService;
import ru.otus.demen.books.service.BookCommentService;
import ru.otus.demen.books.service.BookService;
import ru.otus.demen.books.service.GenreService;

import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = ControllerTestConfiguration.class)
@AutoConfigureMockMvc
class BookControllerTest {
    private static final Genre NOVEL = new Genre(1L, "Роман");
    private static final Author TOLSTOY = new Author(1L, "Лев", "Толстой");
    private static final Book WAR_AND_PEACE = new Book(1L, "Война и мир", TOLSTOY, NOVEL);
    private static final Book ANNA_KARENINA = new Book(2L, "Анна Каренина", TOLSTOY, NOVEL);
    public static final BookComment WAR_AND_PEACE_COMMENT
            = new BookComment(1L, "Объемная книга", WAR_AND_PEACE);

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BookService bookService;

    @Autowired
    BookCommentService bookCommentService;

    @Autowired
    AuthorService authorService;

    @Autowired
    GenreService genreService;

    @Test
    @DisplayName("Успешное отображение списка книг по url /books")
    void books_ok() throws Exception {
        when(bookService.findAll()).thenReturn(List.of(WAR_AND_PEACE, ANNA_KARENINA));
        ResultActions resultActions = mockMvc.perform(get("/books"));
        expectBooks(resultActions);
    }

    private void expectBooks(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString(WAR_AND_PEACE.getName())))
                .andExpect(content().string(containsString(ANNA_KARENINA.getName())));
    }

    @Test
    @DisplayName("Успешное отображение списка книг по url /")
    void books_ok_by_root_url() throws Exception {
        when(bookService.findAll()).thenReturn(List.of(WAR_AND_PEACE, ANNA_KARENINA));
        expectBooks(mockMvc.perform(get("/")));
    }

    @Test
    @DisplayName("Успешное отображение книги с комментарием")
    void viewBook_ok() throws Exception {
        when(bookService.getById(WAR_AND_PEACE.getId())).thenReturn(WAR_AND_PEACE);
        when(bookCommentService.getByBookId(WAR_AND_PEACE.getId())).thenReturn(List.of(WAR_AND_PEACE_COMMENT));
        ResultActions resultActions = mockMvc.perform(get("/book/view?id=" + WAR_AND_PEACE.getId()));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString(WAR_AND_PEACE.getName())))
                .andExpect(content().string(containsString(WAR_AND_PEACE_COMMENT.getText())));
    }

    @Test
    @DisplayName("Для отображения книги не передан id")
    void viewBook_no_id() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/book/view"));
        resultActions.andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Успешное отображение книги для редактирования")
    void editBookGet_ok() throws Exception {
        when(bookService.getById(WAR_AND_PEACE.getId())).thenReturn(WAR_AND_PEACE);
        when(authorService.getAll()).thenReturn(List.of(TOLSTOY));
        when(genreService.getAll()).thenReturn(List.of(NOVEL));
        ResultActions resultActions = mockMvc.perform(get("/book/edit?id=" + WAR_AND_PEACE.getId()));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString(WAR_AND_PEACE.getName())));
    }

    @Test
    @DisplayName("Успешное изменение наименования книги")
    void editBookPost_changeName() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/book/edit?id=" + WAR_AND_PEACE.getId())
                .param("name", ANNA_KARENINA.getName())
                .param("author", String.valueOf(WAR_AND_PEACE.getAuthor().getId()))
                .param("genre", String.valueOf(WAR_AND_PEACE.getGenre().getId()))
        );
        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
        verify(bookService, times(1))
                .update(WAR_AND_PEACE.getId(), ANNA_KARENINA.getName(), WAR_AND_PEACE.getAuthor().getId(),
                        WAR_AND_PEACE.getGenre().getId());
    }
}