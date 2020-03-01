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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = BookController.class)
@AutoConfigureMockMvc
@Import(SecurityTestConfiguration.class)
public class SecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(strings = {"/", "/book/view?id=1"})
    @DisplayName("Перенаправление неавторизованного запроса")
    public void redirect_unidentified_request(String url) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @WithMockUser("testuser")
    @ParameterizedTest
    @ValueSource(strings = {"/", "/book/view?id=1"})
    @DisplayName("Возврат авторизованному запросу статуса 200 ОК")
    public void ok_identified_request(String url) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isOk());
    }
}
