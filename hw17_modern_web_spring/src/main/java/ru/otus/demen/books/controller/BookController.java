package ru.otus.demen.books.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.demen.books.controller.dto.BookCommentDto;
import ru.otus.demen.books.controller.dto.BookDto;
import ru.otus.demen.books.controller.dto.BookInputDto;
import ru.otus.demen.books.controller.dto.mapper.*;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.service.AuthorService;
import ru.otus.demen.books.service.BookCommentService;
import ru.otus.demen.books.service.BookService;
import ru.otus.demen.books.service.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SameReturnValue")
@Slf4j
@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookCommentService bookCommentService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookMappersFacade bookMappers;

    @GetMapping(path = "/api/books")
    public List<BookDto> getBookList() {
        return bookService.findAll().stream().map(bookMappers::toBookDto).collect(Collectors.toList());
    }

    @GetMapping("/api/book/{bookId}/comments")
    public List<BookCommentDto> getBookComments(@PathVariable("bookId") long bookId) {
        return bookCommentService.getByBookId(bookId)
                .stream().map(bookMappers::toBookCommentDto).collect(Collectors.toList());
    }

    @PostMapping("/api/book/{bookId}/comment/add")
    public BookCommentDto addBookComment(@PathVariable("bookId") long bookId, @RequestBody BookCommentDto bookCommentDto)
    {
        BookComment bookComment = bookCommentService.add(bookId, bookCommentDto.getText());
        return bookMappers.toBookCommentDto(bookComment);
    }

    @PutMapping("/api/book/edit/{id}")
    public ResultOk editBook(@PathVariable("id") long id, @RequestBody BookInputDto bookInputDto) {
        log.info("editBook id={} bookInputDto={}", id, bookInputDto);
        bookService.update(id, bookInputDto.getName(), bookInputDto.getAuthorId(), bookInputDto.getGenreId());
        return ResultOk.INSTANCE;
    }


    @PostMapping("/api/book/add")
    public BookDto addBook(@RequestBody BookInputDto bookInputDto)
    {
        log.info("addBook bookInputDto={}", bookInputDto);
        return bookMappers.toBookDto(
                bookService.add(bookInputDto.getName(), bookInputDto.getAuthorId(), bookInputDto.getGenreId()));
    }

    @GetMapping("/book/delete-comment")
    public ResultOk deleteComment(@RequestParam("book_id") long bookId, @RequestParam("comment_id") long commentId)
    {
        bookCommentService.deleteById(commentId);
        return ResultOk.INSTANCE;
    }

    @DeleteMapping("/api/book/delete/{id}")
    public ResultOk deleteBook(@PathVariable("id") long id)
    {
        bookService.deleteById(id);
        return ResultOk.INSTANCE;
    }
}
