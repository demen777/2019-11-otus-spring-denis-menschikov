package ru.otus.demen.books.service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.demen.books.dao.AuthorDao;
import ru.otus.demen.books.dao.BookCommentDao;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.dao.GenreDao;

@TestConfiguration
public class ServiceTestConfiguration {
    @MockBean
    AuthorDao authorDao;

    @MockBean
    BookCommentDao bookCommentDao;

    @MockBean
    BookDao bookDao;

    @MockBean
    GenreDao genreDao;
}
