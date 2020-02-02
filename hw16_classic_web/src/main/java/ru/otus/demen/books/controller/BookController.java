package ru.otus.demen.books.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.service.AuthorService;
import ru.otus.demen.books.service.BookCommentService;
import ru.otus.demen.books.service.BookService;
import ru.otus.demen.books.service.GenreService;

@SuppressWarnings("SameReturnValue")
@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookCommentService bookCommentService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @GetMapping(path = {"/", "/books"})
    public String books(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "books";
    }

    @GetMapping("/book/view")
    public String viewBook(@RequestParam("id") long id, Model model) {
        model.addAttribute("book", bookService.getById(id));
        model.addAttribute("bookComments", bookCommentService.getByBookId(id));
        return "view_book";
    }

    @GetMapping("/book/edit")
    public String editBookGet(@RequestParam("id") long id, Model model) {
        model.addAttribute("book", bookService.getById(id));
        model.addAttribute("authors", authorService.getAll());
        model.addAttribute("genres", genreService.getAll());
        return "edit_book";
    }

    @PostMapping("/book/edit")
    public RedirectView editBookPost(@ModelAttribute Book book) {
        log.info("editBookPost book={}", book);
        bookService.update(book);
        return new RedirectView("/books");
    }
}
