package ru.otus.demen.books.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.demen.books.model.Author;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.BookCommentService;
import ru.otus.demen.books.service.BookService;

import static org.mockito.Mockito.*;


@WebMvcTest(BookController.class)
@Import(ControllerTestConfiguration.class)
class BookControllerTest {
    private static final Genre NOVEL = new Genre("Роман");
    private static final Author TOLSTOY = new Author("Лев", "Толстой");
    private static final Book warAndPeace = new Book("Война и мир", TOLSTOY, NOVEL);
    private static final Book ANNA_KARENINA = new Book("Анна Каренина", TOLSTOY, NOVEL);
    public static final BookComment WAR_AND_PEACE_COMMENT
            = new BookComment("Объемная книга");
    public static final String RESULT_OK_JSON = "{\"result\": \"OK\"}";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    BookService bookService;

    @Autowired
    BookCommentService bookCommentService;

    @Test
    @DisplayName("Успешная выдача списка книг")
    void getBookList_ok() throws Exception {
        when(bookService.findAll()).thenReturn(Flux.just(warAndPeace, ANNA_KARENINA));
        webTestClient.get()
                .uri("/api/books")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$[:2].name", warAndPeace.getName());
        // TODO check Anna Karenina
    }

//    @Test
//    @DisplayName("Успешное добавление комментария")
//    void addComment_ok() throws Exception {
//        when(bookService.getById(warAndPeace.getId())).thenReturn(Mono.just(warAndPeace));
//        when(bookCommentService.add(warAndPeace.getId(), WAR_AND_PEACE_COMMENT.getText()))
//            .thenReturn(Mono.just(WAR_AND_PEACE_COMMENT));
//        String inputJson = String.format("{\"text\": \"%s\"}", WAR_AND_PEACE_COMMENT.getText());
//        String inputUrl = String.format("/api/book/%s/comment", warAndPeace.getId());
//        ResultActions resultActions = mockMvc.perform(post(inputUrl)
//                .content(inputJson)
//                .contentType("application/json"));
//        resultActions.andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(content().json(inputJson));
//    }
//
//    @Test
//    @DisplayName("Успешный ввод новой книги")
//    void addBook_ok() throws Exception {
//        when(bookService.add(ANNA_KARENINA.getName(), warAndPeace.getAuthor().getId(),
//                warAndPeace.getGenre().getId())).thenReturn(Mono.just(ANNA_KARENINA));
//        String inputJson = String.format("{\"name\": \"%s\", \"authorId\": \"%s\", \"genreId\": \"%s\"}",
//                ANNA_KARENINA.getName(), warAndPeace.getAuthor().getId(),
//                warAndPeace.getGenre().getId());
//        ResultActions resultActions = mockMvc.perform(post("/api/book")
//                .content(inputJson)
//                .contentType("application/json"));
//        resultActions.andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(content().string(containsString(ANNA_KARENINA.getName())));
//    }
//
//    @Test
//    @DisplayName("Удаление комментария успешно")
//    void deleteComment_ok() throws Exception {
//        ResultActions resultActions = mockMvc.perform(delete(
//                "/api/book/comment/{bookCommentId}", WAR_AND_PEACE_COMMENT.getId()));
//        resultActions.andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(content().json(RESULT_OK_JSON));
//        verify(bookCommentService, times(1)).deleteById(WAR_AND_PEACE_COMMENT.getId());
//    }
//
//    @Test
//    @DisplayName("Удаление книги успешно")
//    void deleteBook_ok() throws Exception {
//        ResultActions resultActions = mockMvc.perform(delete(
//                "/api/book/{bookId}", warAndPeace.getId()));
//        resultActions.andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(content().json(RESULT_OK_JSON));
//        verify(bookService, times(1)).deleteById(warAndPeace.getId());
//    }
}