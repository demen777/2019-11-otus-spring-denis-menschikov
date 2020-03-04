package ru.otus.demen.books.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import(WebSecurityTestConfiguration.class)
public class GenreControllerSecurityTest {
    public static final String GENRES_URI = "/genres";
    public static final String GET_ADD_GENRE_URI = "/genre/add";
    public static final String POST_ADD_GENRE_URI = "/genre/add?name=abc";

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(username = "user", roles = {"USER", "SPECIAL_BOOK"})
    @ParameterizedTest
    @ValueSource(strings = {GENRES_URI})
    @DisplayName("Ресурсы доступные с ролью USER")
    public void get_with_user_success(String url) throws Exception {
        mockMvc.perform(get(url)).andExpect(status().isOk());
    }

    @WithMockUser(username = "user", roles = "USER")
    @ParameterizedTest
    @ValueSource(strings = {GET_ADD_GENRE_URI})
    @DisplayName("GET ресурсы НЕ доступные с ролью USER")
    public void get_with_user_fail(String url) throws Exception {
        mockMvc.perform(get(url)).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user", roles = "USER")
    @ParameterizedTest
    @ValueSource(strings = {POST_ADD_GENRE_URI})
    @DisplayName("POST ресурсы НЕ доступные с ролью USER")
    public void post_with_user_fail(String url) throws Exception {
        mockMvc.perform(post(url)).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "operator", roles = "OPERATOR")
    @ParameterizedTest
    @ValueSource(strings = {GENRES_URI})
    @DisplayName("Ресурсы доступные с ролью OPERATOR")
    public void get_with_operator_success(String url) throws Exception {
        mockMvc.perform(get(url)).andExpect(status().isOk());
    }

    @WithMockUser(username = "operator", roles = "OPERATOR")
    @ParameterizedTest
    @ValueSource(strings = {GET_ADD_GENRE_URI})
    @DisplayName("GET ресурсы НЕ доступные с ролью OPERATOR")
    public void get_with_operator_fail(String url) throws Exception {
        mockMvc.perform(get(url)).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "operator", roles = "OPERATOR")
    @ParameterizedTest
    @ValueSource(strings = {POST_ADD_GENRE_URI})
    @DisplayName("POST ресурсы НЕ доступные с ролью OPERATOR")
    public void post_with_operator_fail(String url) throws Exception {
        mockMvc.perform(post(url)).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @ParameterizedTest
    @ValueSource(strings = {GENRES_URI, GET_ADD_GENRE_URI})
    @DisplayName("GET ресурсы доступные с ролью ADMIN")
    public void get_with_admin_success(String url) throws Exception {
        mockMvc.perform(get(url)).andExpect(status().isOk());
    }

    @WithMockUser(username = "admin", roles = "ADMIN")
    @ParameterizedTest
    @ValueSource(strings = {POST_ADD_GENRE_URI})
    @DisplayName("POST ресурсы доступные с ролью ADMIN с HTTP Found 302 в ответе")
    public void post_with_operator_redirect(String url) throws Exception {
        mockMvc.perform(post(url)).andExpect(status().isFound())
            .andExpect(redirectedUrl(GENRES_URI));
    }
}
