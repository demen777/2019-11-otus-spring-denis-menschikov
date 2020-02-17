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
    private static final Author TOLSTOY = new Author("Лев", "Толстой");
    public static final String FIRSTNAME_MUST_BE_NOT_EMPTY_MSG = "Имя не должно быть пустым";
    public static final String SURNAME_MUST_BE_NOT_EMPTY_MSG = "Фамилия не должна быть пустой";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthorService authorService;

    @Autowired
    AuthorDtoMapper authorDtoMapper;

    @Test
    @DisplayName("Успешное выдача списка авторов")
    void getAuthorList_ok() throws Exception {
        when(authorService.getAll()).thenReturn(Flux.just(TOLSTOY));
        ResultActions resultActions = mockMvc.perform(get("/api/authors"));
        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(content().string(containsString(TOLSTOY.getSurname())));
    }

    @Test
    @DisplayName("Успешное добавление нового автора")
    void addAuthor_ok() throws Exception {
        when(authorService.add("Лев", "Толстой")).thenReturn(Mono.just(TOLSTOY));
        String inputJson = "{\"firstName\": \"Лев\", \"surname\": \"Толстой\"}";
        ResultActions resultActions = mockMvc.perform(post("/api/author")
                .content(inputJson)
                .contentType("application/json")
        );
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(inputJson));
    }

    @Test
    @DisplayName("Ввод нового автора с пустым именем")
    void addAuthor_emptyFirstName() throws Exception {
        when(authorService.add("", "Толстой"))
                .thenThrow(new IllegalParameterException(FIRSTNAME_MUST_BE_NOT_EMPTY_MSG));
        String inputJson = "{\"firstName\": \"\", \"surname\": \"Толстой\"}";
        ResultActions resultActions = mockMvc.perform(post("/api/author")
                .content(inputJson)
                .contentType("application/json")
        );
        resultActions.andExpect(status().is4xxClientError())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string(containsString(FIRSTNAME_MUST_BE_NOT_EMPTY_MSG)));
    }

    @Test
    @DisplayName("Ввод нового автора с пустой фамилией")
    void addAuthor_emptySurname() throws Exception {
        when(authorService.add("Лев", ""))
                .thenThrow(new IllegalParameterException(SURNAME_MUST_BE_NOT_EMPTY_MSG));
        String inputJson = "{\"firstName\": \"Лев\", \"surname\": \"\"}";
        ResultActions resultActions = mockMvc.perform(post("/api/author")
                .content(inputJson)
                .contentType("application/json")
        );
        resultActions.andExpect(status().is4xxClientError())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string(containsString(SURNAME_MUST_BE_NOT_EMPTY_MSG)));
    }
}