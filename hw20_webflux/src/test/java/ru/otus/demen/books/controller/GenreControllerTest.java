package ru.otus.demen.books.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.GenreService;
import ru.otus.demen.books.service.exception.IllegalParameterException;

import static org.mockito.Mockito.*;
import static ru.otus.demen.books.controller.ControllerTestUtils.createApiExceptionJson;

@WebFluxTest(AuthorController.class)
@Import(ControllerTestConfiguration.class)
class GenreControllerTest {
    private static Genre novel = new Genre("Роман");
    public static final String NOVEL_ID = "1";
    public static final String NAME_MUST_BE_NOT_EMPTY_MSG = "Имя жанра должно быть непустым";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    GenreService genreService;

    @BeforeAll
    static void beforeAll() {
        novel.setId(NOVEL_ID);
    }

    @Test
    @DisplayName("Успешная выдача списка жанров")
    void getAuthorList_ok() throws Exception {
        when(genreService.getAll()).thenReturn(Flux.just(novel));
        String outputJson = String.format("[{\"id\": \"%s\", \"name\": \"Роман\"}]", NOVEL_ID);
        webTestClient.get()
                .uri("/api/genres")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().json(outputJson);
    }

    @Test
    @DisplayName("Успешное добавление нового жанра")
    void addGenre_ok() throws Exception {
        when(genreService.add("Роман")).thenReturn(Mono.just(novel));
        String inputJson = "{\"name\": \"Роман\"}";
        String outputJson = String.format("{\"id\": \"%s\", \"name\": \"Роман\"}", NOVEL_ID);
        webTestClient.post()
                .uri("/api/genre")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(inputJson))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().json(outputJson);
    }

    @Test
    @DisplayName("Ввод нового жанра с пустым наименованием")
    void addGenre_emptyName() throws Exception {
        when(genreService.add(""))
                .thenThrow(new IllegalParameterException(NAME_MUST_BE_NOT_EMPTY_MSG));
        String inputJson = "{\"name\": \"\"}";
        String outputJson = createApiExceptionJson("Ошибка пользовательского ввода",
                NAME_MUST_BE_NOT_EMPTY_MSG);
        webTestClient.post()
                .uri("/api/genre")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(inputJson))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().json(outputJson);
    }
}