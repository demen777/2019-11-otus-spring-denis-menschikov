package ru.otus.demen.books.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.GenreService;
import ru.otus.demen.books.service.exception.IllegalParameterException;

import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GenreController.class)
@Import(ControllerTestConfiguration.class)
class GenreControllerTest {
    private static final Genre NOVEL = new Genre(1L, "Роман");
    public static final String NAME_MUST_BE_NOT_EMPTY_MSG = "Имя жанра должно быть непустым";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    GenreService genreService;

    @Test
    @DisplayName("Успешная выдача списка жанров")
    void getAuthorList_ok() throws Exception {
        when(genreService.getAll()).thenReturn(List.of(NOVEL));
        ResultActions resultActions = mockMvc.perform(get("/api/genres"));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string(containsString(NOVEL.getName())));
    }

    @Test
    @DisplayName("Успешное добавление нового жанра")
    void addGenre_ok() throws Exception {
        when(genreService.add("Роман")).thenReturn(NOVEL);
        String inputJson = "{\"name\": \"Роман\"}";
        ResultActions resultActions = mockMvc.perform(post("/api/genre")
                .content(inputJson)
                .contentType("application/json")
        );
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(inputJson));
    }

    @Test
    @DisplayName("Ввод нового жанра с пустым наименованием")
    void addGenre_emptyName() throws Exception {
        when(genreService.add(""))
                .thenThrow(new IllegalParameterException(NAME_MUST_BE_NOT_EMPTY_MSG));
        String inputJson = "{\"name\": \"\"}";
        ResultActions resultActions = mockMvc.perform(post("/api/genre")
                .content(inputJson)
                .contentType("application/json")
        );
        resultActions.andExpect(status().is4xxClientError())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string(containsString(NAME_MUST_BE_NOT_EMPTY_MSG)));
    }
}