package ru.otus.demen.books.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.demen.books.controller.dto.mapper.AuthorDtoMapper;
import ru.otus.demen.books.controller.dto.mapper.BookCommentDtoMapper;
import ru.otus.demen.books.controller.dto.mapper.BookDtoMapper;
import ru.otus.demen.books.controller.dto.mapper.GenreDtoMapper;
import ru.otus.demen.books.model.Genre;
import ru.otus.demen.books.service.AuthorService;
import ru.otus.demen.books.service.BookCommentService;
import ru.otus.demen.books.service.BookService;
import ru.otus.demen.books.service.GenreService;

import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SuppressWarnings("SameReturnValue")
@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookCommentService bookCommentService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final AuthorDtoMapper authorDtoMapper;
    private final GenreDtoMapper genreDtoMapper;
    private final BookDtoMapper bookDtoMapper;
    private final BookCommentDtoMapper bookCommentDtoMapper;

    @GetMapping(path = {"/", "/books"})
    public String books(Model model) {
        model.addAttribute("books",
            bookService.findAll().stream().map(bookDtoMapper::bookDto).collect(Collectors.toList()));
        return "books";
    }

    @GetMapping("/book/view")
    public String viewBook(@RequestParam("id") long id, Model model) {
        model.addAttribute("book", bookDtoMapper.bookDto(bookService.getById(id)));
        model.addAttribute("bookComments", bookCommentService.getByBookId(id)
            .stream().map(bookCommentDtoMapper::toBookCommentDto).collect(Collectors.toList()));
        return "view_book";
    }

    @GetMapping("/book/edit")
    public String editBookGet(@RequestParam("id") long id, Model model) {
        model.addAttribute("book", bookDtoMapper.bookDto(bookService.getById(id)));
        model.addAttribute("authors",
            authorService.getAll().stream().map(authorDtoMapper::toAuthorDto).collect(Collectors.toList()));
        model.addAttribute("genres",
            genreService.getAll().stream().map(genreDtoMapper::toGenreDto).collect(Collectors.toList()));
        return "edit_book";
    }

    @PostMapping("/book/edit")
    public RedirectView editBookPost(@RequestParam("id") long id, @RequestParam("name") String name,
                                     @RequestParam("author") long author_id, @RequestParam("genre") long genre_id) {
        log.info("editBookPost id={} name={} author_id={} genre_id={}", id, name, author_id, genre_id);
        bookService.update(id, name, author_id, genre_id);
        return new RedirectView("/books");
    }
}
