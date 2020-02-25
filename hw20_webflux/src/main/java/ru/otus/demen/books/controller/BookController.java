package ru.otus.demen.books.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.demen.books.controller.dto.BookCommentDto;
import ru.otus.demen.books.controller.dto.BookDto;
import ru.otus.demen.books.controller.dto.BookInputDto;
import ru.otus.demen.books.controller.dto.mapper.*;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.service.BookCommentService;
import ru.otus.demen.books.service.BookService;

@SuppressWarnings("SameReturnValue")
@Slf4j
@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookCommentService bookCommentService;
    private final BookMappersFacade bookMappers;
    //language=JSON
    public static final String RESULT_OK = "{\"result\": \"OK\"}";

    @GetMapping(path = "/api/books")
    public Flux<BookDto> getBookList() {
        return bookService.findAll().map(bookMappers::toBookDto);
    }

    @GetMapping("/api/book/{bookId}")
    public Mono<BookDto> getBook(@PathVariable("bookId") String bookId) {
        log.info("getBook bookId={}", bookId);
        return bookService.getById(bookId).map(bookMappers::toBookDto);
    }

    @GetMapping("/api/book/{bookId}/comments")
    public Flux<BookCommentDto> getBookComments(@PathVariable("bookId") String bookId) {
        return bookService.getById(bookId).map(Book::getComments).flatMapMany(Flux::fromIterable)
            .map(bookMappers::toBookCommentDto);
    }

    @PostMapping("/api/book/{bookId}/comment")
    public Mono<BookCommentDto> addBookComment(@PathVariable("bookId") String bookId,
                                               @RequestBody BookCommentDto bookCommentDto) {
        return bookCommentService.add(bookId, bookCommentDto.getText()).map(bookMappers::toBookCommentDto);
    }

    @PutMapping(path = "/api/book/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> editBook(@PathVariable("id") String id, @RequestBody BookInputDto bookInputDto) {
        log.info("editBook id={} bookInputDto={}", id, bookInputDto);
        return bookService.update(id, bookInputDto.getName(), bookInputDto.getAuthorId(), bookInputDto.getGenreId())
            .thenReturn(RESULT_OK);
    }


    @PostMapping("/api/book")
    public Mono<BookDto> addBook(@RequestBody BookInputDto bookInputDto) {
        log.info("addBook bookInputDto={}", bookInputDto);
        return bookService.add(bookInputDto.getName(), bookInputDto.getAuthorId(), bookInputDto.getGenreId())
            .map(bookMappers::toBookDto);
    }

    @DeleteMapping(path = "/api/book/comment/{bookCommentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> deleteComment(@PathVariable("bookCommentId") String bookCommentId) {
        return bookCommentService.deleteById(bookCommentId).thenReturn(RESULT_OK);
    }

    @DeleteMapping(value = "/api/book/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> deleteBook(@PathVariable("id") String id) {
        return bookService.deleteById(id).thenReturn(RESULT_OK);
    }
}
