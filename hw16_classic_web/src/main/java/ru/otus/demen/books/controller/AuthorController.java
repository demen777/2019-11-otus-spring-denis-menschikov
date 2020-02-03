package ru.otus.demen.books.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.demen.books.controller.dto.mapper.AuthorDtoMapper;
import ru.otus.demen.books.service.AuthorService;

import java.util.stream.Collectors;

@SuppressWarnings("SameReturnValue")
@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorDtoMapper authorDtoMapper;

    @GetMapping("/authors")
    public ModelAndView authors() {
        ModelAndView modelAndView = new ModelAndView("authors");
        modelAndView.addObject("authors",
            authorService.getAll().stream().map(authorDtoMapper::toAuthorDto).collect(Collectors.toList()));
        return modelAndView;
    }
}

