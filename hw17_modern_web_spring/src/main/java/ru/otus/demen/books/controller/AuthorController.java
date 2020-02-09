package ru.otus.demen.books.controller;

import com.sun.source.doctree.AuthorTree;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.demen.books.controller.dto.AuthorDto;
import ru.otus.demen.books.controller.dto.mapper.AuthorDtoMapper;
import ru.otus.demen.books.service.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SameReturnValue")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorDtoMapper authorDtoMapper;

    @GetMapping("/api/authors")
    public List<AuthorDto> getAuthorList() {
        return
            authorService.getAll().stream().map(authorDtoMapper::toAuthorDto).collect(Collectors.toList());
    }

    @PostMapping("/author/add")
    public RedirectView addAuthor(@RequestParam("firstName") String firstName,
                                  @RequestParam("surname") String surname)
    {
        log.info("addAuthorPost firstName={} surname={}", firstName, surname);
        authorService.add(firstName, surname);
        return new RedirectView("/authors");
    }
}

