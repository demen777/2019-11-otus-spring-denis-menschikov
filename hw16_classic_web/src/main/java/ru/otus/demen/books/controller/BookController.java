package ru.otus.demen.books.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.demen.books.controller.dto.mapper.AuthorDtoMapper;
import ru.otus.demen.books.controller.dto.mapper.BookCommentDtoMapper;
import ru.otus.demen.books.controller.dto.mapper.BookDtoMapper;
import ru.otus.demen.books.controller.dto.mapper.GenreDtoMapper;
import ru.otus.demen.books.service.AuthorService;
import ru.otus.demen.books.service.BookCommentService;
import ru.otus.demen.books.service.BookService;
import ru.otus.demen.books.service.GenreService;

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
    public ModelAndView books() {
        ModelAndView modelAndView = new ModelAndView("books");
        modelAndView.addObject("books",
            bookService.findAll().stream().map(bookDtoMapper::bookDto).collect(Collectors.toList()));
        return modelAndView;
    }

    @GetMapping("/book/view")
    public ModelAndView viewBookGet(@RequestParam("id") long id) {
        return fillModelAndReturnView(id);
    }

    @PostMapping("/book/view")
    public ModelAndView viewBookPost(@RequestParam("id") long id, @RequestParam("comment_text") String commentText)
    {
        bookCommentService.add(id, commentText);
        return fillModelAndReturnView(id);
    }

    private ModelAndView fillModelAndReturnView(@RequestParam("id") long id) {
        ModelAndView modelAndView = new ModelAndView("view_book");
        modelAndView.addObject("book", bookDtoMapper.bookDto(bookService.getById(id)));
        modelAndView.addObject("bookComments", bookCommentService.getByBookId(id)
                .stream().map(bookCommentDtoMapper::toBookCommentDto).collect(Collectors.toList()));
        return modelAndView;
    }

    @GetMapping("/book/edit")
    public ModelAndView editBookGet(@RequestParam("id") long id) {
        ModelAndView modelAndView = new ModelAndView("edit_book");
        modelAndView.addObject("book", bookDtoMapper.bookDto(bookService.getById(id)));
        modelAndView.addObject("authors",
            authorService.getAll().stream().map(authorDtoMapper::toAuthorDto).collect(Collectors.toList()));
        modelAndView.addObject("genres",
            genreService.getAll().stream().map(genreDtoMapper::toGenreDto).collect(Collectors.toList()));
        return modelAndView;
    }

    @PostMapping("/book/edit")
    public RedirectView editBookPost(@RequestParam("id") long id, @RequestParam("name") String name,
                                     @RequestParam("author") long author_id, @RequestParam("genre") long genre_id) {
        log.info("editBookPost id={} name={} author_id={} genre_id={}", id, name, author_id, genre_id);
        bookService.update(id, name, author_id, genre_id);
        return new RedirectView("/books");
    }

    @GetMapping("/book/delete-comment")
    public ModelAndView deleteComment(@RequestParam("book_id") long bookId, @RequestParam("comment_id") long commentId)
    {
        bookCommentService.deleteById(commentId);
        return fillModelAndReturnView(bookId);
    }
}
