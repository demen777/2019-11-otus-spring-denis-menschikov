package ru.otus.demen.books.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.demen.books.controller.dto.mapper.*;
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
    private final BookMappersFacade bookMappers;

    @GetMapping(path = {"/", "/books"})
    public ModelAndView getBookList() {
        ModelAndView modelAndView = new ModelAndView("books");
        modelAndView.addObject("books",
            bookService.findAll().stream().map(bookMappers::bookDto).collect(Collectors.toList()));
        return modelAndView;
    }

    @GetMapping("/book/view")
    public ModelAndView getBook(@RequestParam("id") long id) {
        return fillModelAndReturnView(id);
    }

    @PostMapping("/book/view")
    public ModelAndView addBookComment(@RequestParam("id") long id, @RequestParam("comment_text") String commentText)
    {
        bookCommentService.add(id, commentText);
        return fillModelAndReturnView(id);
    }

    private ModelAndView fillModelAndReturnView(@RequestParam("id") long id) {
        ModelAndView modelAndView = new ModelAndView("view_book");
        modelAndView.addObject("book", bookMappers.bookDto(bookService.getById(id)));
        modelAndView.addObject("bookComments", bookCommentService.getByBookId(id)
                .stream().map(bookMappers::toBookCommentDto).collect(Collectors.toList()));
        return modelAndView;
    }

    @GetMapping("/book/edit")
    public ModelAndView getBookForEdit(@RequestParam("id") long id) {
        ModelAndView modelAndView = new ModelAndView("edit_book");
        modelAndView.addObject("book", bookMappers.bookDto(bookService.getById(id)));
        modelAndView.addObject("authors",
            authorService.getAll().stream().map(bookMappers::toAuthorDto).collect(Collectors.toList()));
        modelAndView.addObject("genres",
            genreService.getAll().stream().map(bookMappers::toGenreDto).collect(Collectors.toList()));
        return modelAndView;
    }

    @PostMapping("/book/edit")
    public RedirectView editBook(@RequestParam("id") long id, @RequestParam("name") String name,
                                 @RequestParam("author") long authorId, @RequestParam("genre") long genreId) {
        log.info("editBookPost id={} name={} author_id={} genre_id={}", id, name, authorId, genreId);
        bookService.update(id, name, authorId, genreId);
        return new RedirectView("/books");
    }

    @GetMapping("/book/add")
    public ModelAndView getFormForNewBook() {
        ModelAndView modelAndView = new ModelAndView("add_book");
        modelAndView.addObject("authors",
                authorService.getAll().stream().map(bookMappers::toAuthorDto).collect(Collectors.toList()));
        modelAndView.addObject("genres",
                genreService.getAll().stream().map(bookMappers::toGenreDto).collect(Collectors.toList()));
        return modelAndView;
    }

    @PostMapping("/book/add")
    public RedirectView addBook(@RequestParam("name") String name,
                                @RequestParam("author") long authorId, @RequestParam("genre") long genreId)
    {
        log.info("addBookPost name={} author_id={} genre_id={}", name, authorId, genreId);
        bookService.add(name, authorId, genreId);
        return new RedirectView("/books");
    }

    @GetMapping("/book/delete-comment")
    public ModelAndView deleteComment(@RequestParam("book_id") long bookId, @RequestParam("comment_id") long commentId)
    {
        bookCommentService.deleteById(commentId);
        return fillModelAndReturnView(bookId);
    }

    @GetMapping("/book/delete")
    public RedirectView deleteBook(@RequestParam("id") long id)
    {
        bookService.deleteById(id);
        return new RedirectView("/books");
    }
}
