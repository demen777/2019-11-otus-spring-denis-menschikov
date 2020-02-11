package ru.otus.demen.books.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
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
import ru.otus.demen.books.service.exception.IllegalParameterException;

import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BookController.class)
@Import(ControllerTestConfiguration.class)
class BookControllerTest {
    private static final Genre NOVEL = new Genre(1L, "Роман");
    private static final Author TOLSTOY = new Author(1L, "Лев", "Толстой");
    private static final Book WAR_AND_PEACE = new Book(1L, "Война и мир", TOLSTOY, NOVEL);
    private static final Book ANNA_KARENINA = new Book(2L, "Анна Каренина", TOLSTOY, NOVEL);
    public static final BookComment WAR_AND_PEACE_COMMENT
        = new BookComment(1L, "Объемная книга", WAR_AND_PEACE);
    private static final String BOOK_MUST_BE_NOT_EMPTY_MSG = "Имя книги должно быть не пустым";

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
    @DisplayName("Успешная выдача списка книг")
    void getBookList_ok() throws Exception {
        when(bookService.findAll()).thenReturn(List.of(WAR_AND_PEACE, ANNA_KARENINA));
        ResultActions resultActions = mockMvc.perform(get("/books"));
        expectBooks(resultActions);
    }

    private void expectBooks(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(content().string(containsString(WAR_AND_PEACE.getName())))
            .andExpect(content().string(containsString(ANNA_KARENINA.getName())))
            .andExpect(model().attributeExists("books"))
            .andExpect(view().name("books"));
    }

    @Test
    @DisplayName("Успешное отображение книги с комментарием")
    void getBook_ok() throws Exception {
        when(bookService.getById(WAR_AND_PEACE.getId())).thenReturn(WAR_AND_PEACE);
        when(bookCommentService.getByBookId(WAR_AND_PEACE.getId())).thenReturn(List.of(WAR_AND_PEACE_COMMENT));
        ResultActions resultActions = mockMvc.perform(get("/book/view?id=" + WAR_AND_PEACE.getId()));
        viewBookExpects(resultActions);
    }

    @Test
    @DisplayName("Успешное добавление комментария")
    void addComment_ok() throws Exception {
        when(bookService.getById(WAR_AND_PEACE.getId())).thenReturn(WAR_AND_PEACE);
        when(bookCommentService.getByBookId(WAR_AND_PEACE.getId())).thenReturn(List.of(WAR_AND_PEACE_COMMENT));
        ResultActions resultActions = mockMvc.perform(post("/book/view?id=" + WAR_AND_PEACE.getId())
            .param("comment_text", WAR_AND_PEACE_COMMENT.getText()));
        viewBookExpects(resultActions);
        verify(bookCommentService, times(1))
            .add(WAR_AND_PEACE.getId(), WAR_AND_PEACE_COMMENT.getText());
    }

    private void viewBookExpects(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(content().string(containsString(WAR_AND_PEACE.getName())))
            .andExpect(content().string(containsString(WAR_AND_PEACE_COMMENT.getText())))
            .andExpect(model().attributeExists("book", "bookComments"))
            .andExpect(view().name("view_book"));
    }

    @Test
    @DisplayName("Для отображения книги не передан id")
    void getBook_no_id() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/book/view"));
        resultActions.andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Успешное изменение наименования книги")
    void editBook_changeName() throws Exception {
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

    @Test
    @DisplayName("Успешный ввод новой книги")
    void addBook_ok() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/book/add")
            .param("name", ANNA_KARENINA.getName())
            .param("author", String.valueOf(WAR_AND_PEACE.getAuthor().getId()))
            .param("genre", String.valueOf(WAR_AND_PEACE.getGenre().getId()))
        );
        resultActions.andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/books"));
        verify(bookService, times(1))
            .add(ANNA_KARENINA.getName(), WAR_AND_PEACE.getAuthor().getId(),
                WAR_AND_PEACE.getGenre().getId());
    }

    @Test
    @DisplayName("Ввод новой книги с пустым наименованием")
    void addBook_emptyName() throws Exception {
        when(bookService.add("", WAR_AND_PEACE.getAuthor().getId(), WAR_AND_PEACE.getGenre().getId()))
            .thenThrow(new IllegalParameterException(BOOK_MUST_BE_NOT_EMPTY_MSG));
        ResultActions resultActions = mockMvc.perform(post("/book/add")
            .param("name", "")
            .param("author", String.valueOf(WAR_AND_PEACE.getAuthor().getId()))
            .param("genre", String.valueOf(WAR_AND_PEACE.getGenre().getId()))
        );
        resultActions.andExpect(status().isOk())
            .andExpect(content().string(containsString(BOOK_MUST_BE_NOT_EMPTY_MSG)))
            .andExpect(view().name("client_error"));
    }

    @Test
    @DisplayName("Удаление комментария успешно")
    void deleteComment_ok() throws Exception {
        when(bookService.getById(WAR_AND_PEACE.getId())).thenReturn(WAR_AND_PEACE);
        ResultActions resultActions = mockMvc.perform(get(
            "/book/delete-comment?book_id=" + WAR_AND_PEACE.getId()
                + "&comment_id=" + WAR_AND_PEACE_COMMENT.getId()));
        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(content().string(containsString(WAR_AND_PEACE.getName())))
            .andExpect(model().attributeExists("book", "bookComments"))
            .andExpect(view().name("view_book"));
        verify(bookCommentService, times(1)).deleteById(WAR_AND_PEACE_COMMENT.getId());
    }

    @Test
    @DisplayName("Удаление книги")
    void deleteBook_ok() throws Exception {
        when(bookService.getById(WAR_AND_PEACE.getId())).thenReturn(WAR_AND_PEACE);
        ResultActions resultActions = mockMvc.perform(get(
            "/book/delete?id=" + WAR_AND_PEACE.getId()));
        resultActions.andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/books"));
        verify(bookService, times(1)).deleteById(WAR_AND_PEACE.getId());
    }
}