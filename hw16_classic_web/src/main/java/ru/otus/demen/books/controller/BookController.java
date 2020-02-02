package ru.otus.demen.books.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.model.BookComment;
import ru.otus.demen.books.service.BookCommentService;
import ru.otus.demen.books.service.BookService;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("SameReturnValue")
@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final BookCommentService bookCommentService;

    @GetMapping(path = {"/", "/books"})
    public String books(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/book/view")
    public String books(@RequestParam("id") long id, Model model) {
        Book book = bookService.getById(id);
        Collection<BookComment> bookComments = bookCommentService.getByBookId(id);
        model.addAttribute("book", book);
        model.addAttribute("bookComments", bookComments);
        return "view_book";
    }
}
