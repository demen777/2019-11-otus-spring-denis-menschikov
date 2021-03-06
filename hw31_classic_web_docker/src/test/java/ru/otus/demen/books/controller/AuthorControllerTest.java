package ru.otus.demen.books.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.otus.demen.books.controller.dto.mapper.AuthorDtoMapper;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.service.AuthorService;
import ru.otus.demen.books.service.exception.IllegalParameterException;

import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
@Import(ControllerTestConfiguration.class)
class AuthorControllerTest {
    private static final Author TOLSTOY = new Author(1L, "Лев", "Толстой");
    public static final String FIRSTNAME_MUST_BE_NOT_EMPTY_MSG = "Имя не должно быть пустым";
    public static final String SURNAME_MUST_BE_NOT_EMPTY_MSG = "Фамилия не должна быть пустой";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthorService authorService;

    @Autowired
    AuthorDtoMapper authorDtoMapper;

    @Test
    @DisplayName("Успешное отображение списка авторов")
    void getAuthorList_ok() throws Exception {
        when(authorService.getAll()).thenReturn(List.of(TOLSTOY));
        ResultActions resultActions = mockMvc.perform(get("/authors"));
        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(model().attributeExists("authors"))
            .andExpect(content().string(containsString(TOLSTOY.getSurname())))
            .andExpect(view().name("authors"));
    }

    @Test
    @DisplayName("Успешное отображение формы для ввода нового автора")
    void getFormForNewAuthor_ok() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/author/add"));
        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("add_author"));
    }

    @Test
    @DisplayName("Успешный submit формы для ввода нового автора")
    void addAuthor_ok() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/author/add")
            .param("firstName", TOLSTOY.getFirstName())
            .param("surname", TOLSTOY.getSurname())
        );
        resultActions.andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/authors"));
        verify(authorService, times(1))
            .add(TOLSTOY.getFirstName(), TOLSTOY.getSurname());
    }

    @Test
    @DisplayName("Ввод нового автора с пустым именем")
    void addAuthor_emptyFirstName() throws Exception {
        when(authorService.add("", TOLSTOY.getSurname()))
            .thenThrow(new IllegalParameterException(FIRSTNAME_MUST_BE_NOT_EMPTY_MSG));
        ResultActions resultActions = mockMvc.perform(post("/author/add")
            .param("firstName", "")
            .param("surname", TOLSTOY.getSurname())
        );
        resultActions.andExpect(status().isOk())
            .andExpect(content().string(containsString(FIRSTNAME_MUST_BE_NOT_EMPTY_MSG)))
            .andExpect(view().name("client_error"));
    }

    @Test
    @DisplayName("Ввод нового автора с пустой фамилией")
    void addAuthor_emptySurname() throws Exception {
        when(authorService.add(TOLSTOY.getFirstName(), ""))
            .thenThrow(new IllegalParameterException(SURNAME_MUST_BE_NOT_EMPTY_MSG));
        ResultActions resultActions = mockMvc.perform(post("/author/add")
            .param("firstName", TOLSTOY.getFirstName())
            .param("surname", "")
        );
        resultActions.andExpect(status().isOk())
            .andExpect(content().string(containsString(SURNAME_MUST_BE_NOT_EMPTY_MSG)))
            .andExpect(view().name("client_error"));
    }
}