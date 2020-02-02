package ru.otus.demen.books.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.BookService;

import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookController.class)
class BookControllerTest {
    private static final Genre NOVEL = new Genre(1L, "Роман");
    private static final Author TOLSTOY = new Author(1L, "Лев", "Толстой");
    private static final Book WAR_AND_PEACE = new Book(1L, "Война и мир", TOLSTOY, NOVEL);
    private static final Book ANNA_KARENINA = new Book(2L, "Анна Каренина", TOLSTOY, NOVEL);

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookService bookService;

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
}