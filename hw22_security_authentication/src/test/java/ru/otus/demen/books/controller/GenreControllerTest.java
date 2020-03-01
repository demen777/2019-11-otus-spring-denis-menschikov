package ru.otus.demen.books.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.GenreService;
import ru.otus.demen.books.service.exception.AlreadyExistsException;
import ru.otus.demen.books.service.exception.IllegalParameterException;

import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GenreController.class)
@WithMockUser(value = "testuser")
@Import(ControllerTestConfiguration.class)
class GenreControllerTest {
    private static final Genre NOVEL = new Genre(1L, "Роман");
    public static final String NAME_MUST_BE_NOT_EMPTY_MSG = "Имя жанра должно быть непустым";
    public static final String NAME_ALREADY_EXISTS = "Жанр с именем " + NOVEL.getName() + " уже есть в БД";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    GenreService genreService;

    @Test
    @DisplayName("Успешное отображение списка жанров")
    void getAuthorList_ok() throws Exception {
        when(genreService.getAll()).thenReturn(List.of(NOVEL));
        ResultActions resultActions = mockMvc.perform(get("/genres"));
        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(content().string(containsString(NOVEL.getName())))
            .andExpect(model().attributeExists("genres"))
            .andExpect(view().name("genres"));
    }

    @Test
    @DisplayName("Успешное отображение формы для ввода нового жанра")
    void getFormForNewAuthor_ok() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/genre/add"));
        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(view().name("add_genre"));
    }

    @Test
    @DisplayName("Успешный submit формы для ввода нового жанра")
    void addGenre_ok() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/genre/add")
            .param("name", NOVEL.getName())
        );
        resultActions.andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/genres"));
        verify(genreService, times(1))
            .add(NOVEL.getName());
    }

    @Test
    @DisplayName("Ввод нового жанра с пустым наименованием")
    void addGenre_emptyName() throws Exception {
        when(genreService.add(""))
            .thenThrow(new IllegalParameterException(NAME_MUST_BE_NOT_EMPTY_MSG));
        ResultActions resultActions = mockMvc.perform(post("/genre/add")
            .param("name", "")
        );
        resultActions.andExpect(status().isOk())
            .andExpect(content().string(containsString(NAME_MUST_BE_NOT_EMPTY_MSG)))
            .andExpect(view().name("client_error"));
    }

    @Test
    @DisplayName("Ввод нового жанра с наименованием которое уже есть в БД")
    void addGenre_alreadyExists() throws Exception {
        when(genreService.add(NOVEL.getName()))
            .thenThrow(new AlreadyExistsException(NAME_ALREADY_EXISTS));
        ResultActions resultActions = mockMvc.perform(post("/genre/add")
            .param("name", NOVEL.getName())
        );
        resultActions.andExpect(status().isOk())
            .andExpect(content().string(containsString(NAME_ALREADY_EXISTS)))
            .andExpect(view().name("client_error"));
    }
}