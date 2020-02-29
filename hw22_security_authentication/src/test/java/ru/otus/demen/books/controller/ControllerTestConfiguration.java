package ru.otus.demen.books.controller;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.demen.books.service.AuthorService;
import ru.otus.demen.books.service.BookCommentService;
import ru.otus.demen.books.service.BookService;
import ru.otus.demen.books.service.GenreService;


@TestConfiguration
@ComponentScan(basePackages = {"ru.otus.demen.books.controller", "ru.otus.demen.books.controller.dto.mapper"})
public class ControllerTestConfiguration {
    @MockBean
    BookService bookService;

    @MockBean
    BookCommentService bookCommentService;

    @MockBean
    AuthorService authorService;

    @MockBean
    GenreService genreService;

//    @Bean
//    UserDetailsService userDetailsService() {
//        //noinspection Convert2Lambda
//        return new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//                if ("testuser".equals(username)) {
//                    return new UserDetailsImpl("testuser",
//                            "$2y$12$086fsoQB5wZJHgfpI.tZFe3syUsrDNu0/FLlePfNc4Wh8CD6wxfjO", // tp
//                            true,
//                            new GrantedAuthorityImpl("USER")
//                    );
//                }
//                throw new UsernameNotFoundException("User not found");
//            }
//        };
//    }
}
