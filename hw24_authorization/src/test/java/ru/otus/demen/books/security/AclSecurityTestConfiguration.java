package ru.otus.demen.books.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.otus.demen.books.dao.AuthorDao;
import ru.otus.demen.books.dao.BookCommentDao;
import ru.otus.demen.books.dao.BookDao;
import ru.otus.demen.books.dao.GenreDao;
import ru.otus.demen.books.service.*;


@TestConfiguration
@Import(value = {AclConfiguration.class, WebSecurityConfiguration.class, ServiceTestConfiguration.class})
public class AclSecurityTestConfiguration {
}
