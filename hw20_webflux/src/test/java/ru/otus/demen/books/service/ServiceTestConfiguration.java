package ru.otus.demen.books.service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.demen.books.dao.AuthorDao;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.dao.GenreDao;

@TestConfiguration
@ComponentScan("ru.otus.demen.books.service")
public class ServiceTestConfiguration {
    @MockBean
    AuthorDao authorDao;

    @MockBean
    BookDao bookDao;

    @MockBean
    GenreDao genreDao;
}
