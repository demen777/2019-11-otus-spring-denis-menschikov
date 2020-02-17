package ru.otus.demen.books.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BookController.class)
@Import(ControllerTestConfiguration.class)
class BookControllerTest {
    private static final Genre NOVEL = new Genre("Роман");
    private static final Author TOLSTOY = new Author("Лев", "Толстой");
    private static final Book WAR_AND_PEACE = new Book("Война и мир", TOLSTOY, NOVEL);
    private static final Book ANNA_KARENINA = new Book("Анна Каренина", TOLSTOY, NOVEL);
    public static final BookComment WAR_AND_PEACE_COMMENT
            = new BookComment("Объемная книга");
    public static final String RESULT_OK_JSON = "{\"result\": \"OK\"}";

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
        when(bookService.findAll()).thenReturn(Flux.just(WAR_AND_PEACE, ANNA_KARENINA));
        ResultActions resultActions = mockMvc.perform(get("/api/books"));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string(containsString(TOLSTOY.getSurname())))
                .andExpect(content().string(containsString(ANNA_KARENINA.getName())));
    }

    @Test
    @DisplayName("Успешное добавление комментария")
    void addComment_ok() throws Exception {
        when(bookService.getById(WAR_AND_PEACE.getId())).thenReturn(Mono.just(WAR_AND_PEACE));
        when(bookCommentService.add(WAR_AND_PEACE.getId(), WAR_AND_PEACE_COMMENT.getText()))
            .thenReturn(Mono.just(WAR_AND_PEACE_COMMENT));
        String inputJson = String.format("{\"text\": \"%s\"}", WAR_AND_PEACE_COMMENT.getText());
        String inputUrl = String.format("/api/book/%s/comment", WAR_AND_PEACE.getId());
        ResultActions resultActions = mockMvc.perform(post(inputUrl)
                .content(inputJson)
                .contentType("application/json"));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(inputJson));
    }

    @Test
    @DisplayName("Успешный ввод новой книги")
    void addBook_ok() throws Exception {
        when(bookService.add(ANNA_KARENINA.getName(), WAR_AND_PEACE.getAuthor().getId(),
                WAR_AND_PEACE.getGenre().getId())).thenReturn(Mono.just(ANNA_KARENINA));
        String inputJson = String.format("{\"name\": \"%s\", \"authorId\": \"%s\", \"genreId\": \"%s\"}",
                ANNA_KARENINA.getName(), WAR_AND_PEACE.getAuthor().getId(),
                WAR_AND_PEACE.getGenre().getId());
        ResultActions resultActions = mockMvc.perform(post("/api/book")
                .content(inputJson)
                .contentType("application/json"));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string(containsString(ANNA_KARENINA.getName())));
    }

    @Test
    @DisplayName("Удаление комментария успешно")
    void deleteComment_ok() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete(
                "/api/book/comment/{bookCommentId}", WAR_AND_PEACE_COMMENT.getId()));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(RESULT_OK_JSON));
        verify(bookCommentService, times(1)).deleteById(WAR_AND_PEACE_COMMENT.getId());
    }

    @Test
    @DisplayName("Удаление книги успешно")
    void deleteBook_ok() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete(
                "/api/book/{bookId}", WAR_AND_PEACE.getId()));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(RESULT_OK_JSON));
        verify(bookService, times(1)).deleteById(WAR_AND_PEACE.getId());
    }
}