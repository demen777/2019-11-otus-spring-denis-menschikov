package ru.otus.demen.books.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.GenreService;

import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ControllerTestConfiguration.class)
@AutoConfigureMockMvc
class GenreControllerTest {
    private static final Genre NOVEL = new Genre(1L, "Роман");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    GenreService genreService;

    @Test
    @DisplayName("Успешное отображение списка жанров")
    void authors_ok() throws Exception {
        when(genreService.getAll()).thenReturn(List.of(NOVEL));
        ResultActions resultActions = mockMvc.perform(get("/genres"));
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString(NOVEL.getName())));
    }
}