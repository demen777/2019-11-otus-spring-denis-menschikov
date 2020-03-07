package ru.otus.demen.books.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.demen.books.controller.BookController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = BookController.class)
@AutoConfigureMockMvc
@Import(SecurityTestConfiguration.class)
public class BookControllerSecurityTest {
    private static final String GET_VIEW_BOOK_URI = "/book/view?id=1";
    private static final String GET_EDIT_BOOK_URI = "/book/edit?id=1";
    private static final String GET_ADD_BOOK_URI = "/book/add";
    private static final String GET_DELETE_BOOK_COMMENT_URI = "/book/delete-comment?book_id=1&comment_id=1";
    private static final String GET_DELETE_BOOK_URI = "/book/delete?id=1";
    private static final String POST_ADD_COMMENT_URI = "/book/view?id=1&comment_text=good";
    private static final String POST_EDIT_BOOK_URI = "/book/edit?id=1&name=a&author=1&genre=1";
    private static final String POST_ADD_BOOK_URI = "/book/add?name=a&author=1&genre=1";
    private static final String LOGIN_URI = "http://localhost/login";
    private static final String BOOKS_URI = "/books";
    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(strings = {"/", GET_VIEW_BOOK_URI})
    @DisplayName("Перенаправление неавторизованного запроса GET")
    public void get_redirect_unidentified_request(String url) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(LOGIN_URI));
    }

    @ParameterizedTest
    @ValueSource(strings = {POST_ADD_COMMENT_URI})
    @DisplayName("Перенаправление неавторизованного запроса POST")
    public void post_redirect_unidentified_request(String url) throws Exception {
        mockMvc.perform(post(url))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(LOGIN_URI));
    }


    @WithMockUser(username = "testuser", roles = "USER")
    @ParameterizedTest
    @ValueSource(strings = {"/", GET_VIEW_BOOK_URI})
    @DisplayName("GET ресурсы доступные с ролью USER")
    public void get_with_user_success(String url) throws Exception {
        mockMvc.perform(get(url)).andExpect(status().isOk());
    }

    @WithMockUser(username = "testuser", roles = "USER")
    @ParameterizedTest
    @ValueSource(strings = {GET_EDIT_BOOK_URI, GET_ADD_BOOK_URI, GET_DELETE_BOOK_COMMENT_URI, GET_DELETE_BOOK_URI})
    @DisplayName("GET ресурсы НЕ доступные с ролью USER")
    public void get_with_user_fail(String url) throws Exception {
        mockMvc.perform(get(url)).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "testuser", roles = "USER")
    @ParameterizedTest
    @ValueSource(strings = {POST_ADD_COMMENT_URI})
    @DisplayName("POST ресурсы доступные с ролью USER")
    public void post_with_user_success(String url) throws Exception {
        mockMvc.perform(post(url)).andExpect(status().isOk());
    }

    @WithMockUser(username = "testuser", roles = "USER")
    @ParameterizedTest
    @ValueSource(strings = {POST_EDIT_BOOK_URI, POST_ADD_BOOK_URI})
    @DisplayName("POST ресурсы НЕ доступные с ролью USER")
    public void post_with_user_fail(String url) throws Exception {
        mockMvc.perform(post(url)).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "testuser", roles = "OPERATOR")
    @ParameterizedTest
    @ValueSource(strings = {"/", GET_VIEW_BOOK_URI, GET_EDIT_BOOK_URI, GET_ADD_BOOK_URI})
    @DisplayName("GET ресурсы доступные с ролью OPERATOR")
    public void get_with_operator_success(String url) throws Exception {
        mockMvc.perform(get(url)).andExpect(status().isOk());
    }

    @WithMockUser(username = "testuser", roles = "OPERATOR")
    @ParameterizedTest
    @ValueSource(strings = {GET_DELETE_BOOK_COMMENT_URI, GET_DELETE_BOOK_URI})
    @DisplayName("GET ресурсы НЕ доступные с ролью OPERATOR")
    public void get_with_operator_fail(String url) throws Exception {
        mockMvc.perform(get(url)).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "testuser", roles = "OPERATOR")
    @ParameterizedTest
    @ValueSource(strings = {POST_ADD_COMMENT_URI})
    @DisplayName("POST ресурсы доступные с ролью OPERATOR")
    public void post_with_operator_success(String url) throws Exception {
        mockMvc.perform(post(url)).andExpect(status().isOk());
    }

    @WithMockUser(username = "testuser", roles = "OPERATOR")
    @ParameterizedTest
    @ValueSource(strings = {POST_EDIT_BOOK_URI, POST_ADD_BOOK_URI})
    @DisplayName("POST ресурсы доступные с ролью OPERATOR с HTTP Found 302 в ответе")
    public void post_with_operator_redirect(String url) throws Exception {
        mockMvc.perform(post(url)).andExpect(status().isFound())
                .andExpect(redirectedUrl(BOOKS_URI));
    }

    @WithMockUser(username = "testuser", roles = "ADMIN")
    @ParameterizedTest
    @ValueSource(strings = {
            "/",
            GET_VIEW_BOOK_URI,
            GET_EDIT_BOOK_URI,
            GET_ADD_BOOK_URI,
            GET_DELETE_BOOK_COMMENT_URI
    })
    @DisplayName("GET ресурсы доступные с ролью ADMIN")
    public void get_with_admin_success(String url) throws Exception {
        mockMvc.perform(get(url)).andExpect(status().isOk());
    }

    @WithMockUser(username = "testuser", roles = "ADMIN")
    @ParameterizedTest
    @ValueSource(strings = {GET_DELETE_BOOK_URI})
    @DisplayName("GET ресурсы доступные с ролью ADMIN с HTTP Found 302 в ответе")
    public void get_with_admin_redirect(String url) throws Exception {
        mockMvc.perform(get(url)).andExpect(status().isFound())
                .andExpect(redirectedUrl(BOOKS_URI));
    }

    @WithMockUser(username = "testuser", roles = "ADMIN")
    @ParameterizedTest
    @ValueSource(strings = {POST_ADD_COMMENT_URI})
    @DisplayName("POST ресурсы доступные с ролью ADMIN")
    public void post_with_admin_success(String url) throws Exception {
        mockMvc.perform(post(url)).andExpect(status().isOk());
    }

    @WithMockUser(username = "testuser", roles = "ADMIN")
    @ParameterizedTest
    @ValueSource(strings = {POST_EDIT_BOOK_URI, POST_ADD_BOOK_URI})
    @DisplayName("POST ресурсы доступные с ролью ADMIN с HTTP Found 302 в ответе")
    public void post_with_admin_redirect(String url) throws Exception {
        mockMvc.perform(post(url)).andExpect(status().isFound())
                .andExpect(redirectedUrl(BOOKS_URI));
    }
}

