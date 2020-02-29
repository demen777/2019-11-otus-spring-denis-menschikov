package ru.otus.demen.books.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.demen.books.service.AuthorService;
import ru.otus.demen.books.service.BookCommentService;
import ru.otus.demen.books.service.BookService;
import ru.otus.demen.books.service.GenreService;


@TestConfiguration
@ComponentScan(basePackages = {"ru.otus.demen.books.controller.dto.mapper"})
@Import(SecurityConfiguration.class)
public class SecurityTestConfiguration {
    @MockBean
    BookService bookService;

    @MockBean
    BookCommentService bookCommentService;

    @MockBean
    AuthorService authorService;

    @MockBean
    GenreService genreService;

    @MockBean
    UserDetailsService userDetailsService;
}
