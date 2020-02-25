package ru.otus.demen.books.controller;

import org.junit.jupiter.api.BeforeEach;
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
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.BookCommentService;
import ru.otus.demen.books.service.BookService;

import static org.mockito.Mockito.*;


@WebFluxTest(BookController.class)
@Import(ControllerTestConfiguration.class)
class BookControllerTest {
    public static final String RESULT_OK_JSON = "{\"result\": \"OK\"}";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    BookService bookService;

    @Autowired
    BookCommentService bookCommentService;

    private Book annaKareninaWithId;
    private Book warAndPeaceWithId;
    public BookComment warAndPeaceCommentWithId;

    @BeforeEach
    void setUp() {
        Author tolstoyAuthor = new Author("Лев", "Толстой");
        tolstoyAuthor.setId("1");
        Genre novelGenre = new Genre("Роман");
        novelGenre.setId("1");
        warAndPeaceWithId =
            new Book("Война и мир", tolstoyAuthor, novelGenre);
        warAndPeaceWithId.setId("1");
        annaKareninaWithId = new Book("Анна Каренина", tolstoyAuthor, novelGenre);
        annaKareninaWithId.setId("2");
        warAndPeaceCommentWithId = new BookComment("Объемная книга");
        warAndPeaceCommentWithId.setId("1");
    }

    @Test
    @DisplayName("Успешная выдача списка книг")
    void getBookList_ok() {
        when(bookService.findAll()).thenReturn(Flux.just(warAndPeaceWithId, annaKareninaWithId));
        webTestClient.get()
            .uri("/api/books")
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$[0].name").isEqualTo(warAndPeaceWithId.getName())
            .jsonPath("$[1].name").isEqualTo(annaKareninaWithId.getName());
    }

    @Test
    @DisplayName("Успешное добавление комментария")
    void addComment_ok() {
        when(bookService.getById(warAndPeaceWithId.getId())).thenReturn(Mono.just(warAndPeaceWithId));
        when(bookCommentService.add(warAndPeaceWithId.getId(), warAndPeaceCommentWithId.getText()))
            .thenReturn(Mono.just(warAndPeaceCommentWithId));
        String inputJson = String.format("{\"text\": \"%s\"}", warAndPeaceCommentWithId.getText());
        String inputUrl = String.format("/api/book/%s/comment", warAndPeaceWithId.getId());
        webTestClient.post()
            .uri(inputUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(inputJson))
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.text").isEqualTo(warAndPeaceCommentWithId.getText());
    }

    @Test
    @DisplayName("Успешный ввод новой книги")
    void addBook_ok() {
        when(bookService.add(annaKareninaWithId.getName(), annaKareninaWithId.getAuthor().getId(),
                annaKareninaWithId.getGenre().getId())).thenReturn(Mono.just(annaKareninaWithId));
        String inputJson = String.format("{\"name\": \"%s\", \"authorId\": \"%s\", \"genreId\": \"%s\"}",
                annaKareninaWithId.getName(), annaKareninaWithId.getAuthor().getId(),
                annaKareninaWithId.getGenre().getId());
        webTestClient.post()
            .uri("/api/book")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(inputJson))
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").isEqualTo(annaKareninaWithId.getId())
            .jsonPath("$.name").isEqualTo(annaKareninaWithId.getName())
            .jsonPath("$.genre.id").isEqualTo(annaKareninaWithId.getGenre().getId())
            .jsonPath("$.genre.name").isEqualTo(annaKareninaWithId.getGenre().getName())
            .jsonPath("$.author.id").isEqualTo(annaKareninaWithId.getAuthor().getId())
            .jsonPath("$.author.firstName").isEqualTo(annaKareninaWithId.getAuthor().getFirstName())
            .jsonPath("$.author.surname").isEqualTo(annaKareninaWithId.getAuthor().getSurname());
    }

    @Test
    @DisplayName("Удаление комментария успешно")
    void deleteComment_ok() {
        when(bookCommentService.deleteById(warAndPeaceCommentWithId.getId())).thenReturn(Mono.just(true));
        webTestClient.delete()
            .uri(String.format("/api/book/comment/%s", warAndPeaceCommentWithId.getId()))
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType("application/json;charset=UTF-8")
            .expectBody()
            .json(RESULT_OK_JSON);
    }

    @Test
    @DisplayName("Удаление книги успешно")
    void deleteBook_ok() {
        when(bookService.deleteById(warAndPeaceWithId.getId())).thenReturn(Mono.empty());
        webTestClient.delete()
            .uri(String.format("/api/book/%s", warAndPeaceWithId.getId()))
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType("application/json;charset=UTF-8")
            .expectBody()
            .json(RESULT_OK_JSON);
    }
}