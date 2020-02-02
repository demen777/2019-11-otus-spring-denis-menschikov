package ru.otus.demen.books.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.demen.books.model.Book;
import ru.otus.demen.books.service.BookService;

import java.util.List;

@SuppressWarnings("SameReturnValue")
@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/books")
    public String books(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books";
    }
}
