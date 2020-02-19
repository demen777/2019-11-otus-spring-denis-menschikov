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
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.service.AuthorService;
import ru.otus.demen.books.service.exception.IllegalParameterException;

import static org.mockito.Mockito.when;
import static ru.otus.demen.books.controller.ControllerTestUtils.createApiExceptionJson;

@WebFluxTest(AuthorController.class)
@Import(ControllerTestConfiguration.class)
class AuthorControllerTest {
    private static final Author tolstoy = new Author("Лев", "Толстой");
    public static final String TOLSTOY_ID = "1";
    public static final String FIRSTNAME_MUST_BE_NOT_EMPTY_MSG = "Имя не должно быть пустым";
    public static final String SURNAME_MUST_BE_NOT_EMPTY_MSG = "Фамилия не должна быть пустой";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    AuthorService authorService;

    @BeforeAll
    static void beforeAll() {
        tolstoy.setId(TOLSTOY_ID);
    }

    @Test
    @DisplayName("Успешное выдача списка авторов")
    void getAuthorList_ok() {
        when(authorService.getAll()).thenReturn(Flux.just(tolstoy));
        String outputJson = String.format("[{\"id\": \"%s\", \"firstName\": \"Лев\", \"surname\": \"Толстой\"}]",
                TOLSTOY_ID);
        webTestClient.get()
                .uri("/api/authors")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().json(outputJson);
    }

    @Test
    @DisplayName("Успешное добавление нового автора")
    void addAuthor_ok() {
        when(authorService.add("Лев", "Толстой")).thenReturn(Mono.just(tolstoy));
        String inputJson = "{\"firstName\": \"Лев\", \"surname\": \"Толстой\"}";
        String outputJson = String.format("{\"id\": \"%s\", \"firstName\": \"Лев\", \"surname\": \"Толстой\"}",
                TOLSTOY_ID);
        webTestClient.post()
                .uri("/api/author")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(inputJson))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().json(outputJson);
    }

    @Test
    @DisplayName("Ввод нового автора с пустым именем")
    void addAuthor_emptyFirstName() {
        when(authorService.add("", "Толстой"))
                .thenThrow(new IllegalParameterException(FIRSTNAME_MUST_BE_NOT_EMPTY_MSG));
        String inputJson = "{\"firstName\": \"\", \"surname\": \"Толстой\"}";
        String outputJson = createApiExceptionJson("Ошибка пользовательского ввода",
                FIRSTNAME_MUST_BE_NOT_EMPTY_MSG);
        webTestClient.post()
                .uri("/api/author")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(inputJson))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().json(outputJson);
    }

    @Test
    @DisplayName("Ввод нового автора с пустой фамилией")
    void addAuthor_emptySurname() {
        when(authorService.add("Лев", ""))
                .thenThrow(new IllegalParameterException(SURNAME_MUST_BE_NOT_EMPTY_MSG));
        String inputJson = "{\"firstName\": \"Лев\", \"surname\": \"\"}";
        String outputJson = createApiExceptionJson("Ошибка пользовательского ввода",
                SURNAME_MUST_BE_NOT_EMPTY_MSG);
        webTestClient.post()
                .uri("/api/author")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(inputJson))
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().json(outputJson);
    }
}