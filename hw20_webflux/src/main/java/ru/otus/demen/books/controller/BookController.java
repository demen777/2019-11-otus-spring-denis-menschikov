package ru.otus.demen.books.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.otus.demen.books.controller.dto.BookCommentDto;
import ru.otus.demen.books.controller.dto.BookDto;
import ru.otus.demen.books.controller.dto.BookInputDto;
import ru.otus.demen.books.controller.dto.mapper.*;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.service.BookCommentService;
import ru.otus.demen.books.service.BookService;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SameReturnValue")
@Slf4j
@RestController
@RequiredArgsConstructor
public class BookController {
    private static final String PRODUCED_CONTENT_TYPE = "application/json;charset=UTF-8";
    private final BookService bookService;
    private final BookCommentService bookCommentService;
    private final BookMappersFacade bookMappers;
    //language=JSON
    public static final String RESULT_OK = "{\"result\": \"OK\"}";

    @GetMapping(path = "/api/books")
    public List<BookDto> getBookList() {
        return bookService.findAll().stream().map(bookMappers::toBookDto).collect(Collectors.toList());
    }

    @GetMapping("/api/book/{bookId}")
    public BookDto getBook(@PathVariable("bookId") String bookId) {
        log.info("getBook bookId={}", bookId);
        return bookMappers.toBookDto(bookService.getById(bookId));
    }

    @GetMapping("/api/book/{bookId}/comments")
    public List<BookCommentDto> getBookComments(@PathVariable("bookId") String bookId) {
        return bookCommentService.getByBookId(bookId)
                .stream().map(bookMappers::toBookCommentDto).collect(Collectors.toList());
    }

    @PostMapping("/api/book/{bookId}/comment")
    public BookCommentDto addBookComment(@PathVariable("bookId") String bookId,
                                         @RequestBody BookCommentDto bookCommentDto)
    {
        BookComment bookComment = bookCommentService.add(bookId, bookCommentDto.getText());
        return bookMappers.toBookCommentDto(bookComment);
    }

    @PutMapping(path = "/api/book/{id}", produces = PRODUCED_CONTENT_TYPE)
    public String editBook(@PathVariable("id") String id, @RequestBody BookInputDto bookInputDto) {
        log.info("editBook id={} bookInputDto={}", id, bookInputDto);
        bookService.update(id, bookInputDto.getName(), bookInputDto.getAuthorId(), bookInputDto.getGenreId());
        return RESULT_OK;
    }


    @PostMapping("/api/book")
    public BookDto addBook(@RequestBody BookInputDto bookInputDto)
    {
        log.info("addBook bookInputDto={}", bookInputDto);
        return bookMappers.toBookDto(
                bookService.add(bookInputDto.getName(), bookInputDto.getAuthorId(), bookInputDto.getGenreId()));
    }

    @DeleteMapping(path = "/api/book/comment/{bookCommentId}", produces = PRODUCED_CONTENT_TYPE)
    public String deleteComment(@PathVariable("bookCommentId") String bookCommentId)
    {
        bookCommentService.deleteById(bookCommentId);
        return RESULT_OK;
    }

    @DeleteMapping(value = "/api/book/{id}", produces = PRODUCED_CONTENT_TYPE)
    public String deleteBook(@PathVariable("id") String id)
    {
        bookService.deleteById(id);
        return RESULT_OK;
    }
}
