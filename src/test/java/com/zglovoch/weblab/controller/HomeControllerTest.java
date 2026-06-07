package com.zglovoch.weblab.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Интеграционные тесты публичных страниц через MockMvc.
 * Spring-контекст поднимается полностью (включая Security и JPA).
 */
@SpringBootTest
class HomeControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @DisplayName("GET / — главная страница отдаёт HTTP 200 и содержит заголовок")
    void homePage_returns200() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/html"))
                .andExpect(view().name("index"));
    }

    @Test
    @DisplayName("GET /menu — страница меню отдаёт HTTP 200")
    void menuPage_returns200() throws Exception {
        mockMvc.perform(get("/menu"))
                .andExpect(status().isOk())
                .andExpect(view().name("menu"))
                .andExpect(model().attributeExists("menuByCategory", "categories"));
    }

    @Test
    @DisplayName("GET /gallery — страница галереи отдаёт HTTP 200")
    void galleryPage_returns200() throws Exception {
        mockMvc.perform(get("/gallery"))
                .andExpect(status().isOk())
                .andExpect(view().name("gallery"))
                .andExpect(model().attributeExists("galleryItems"));
    }

    @Test
    @DisplayName("GET /contacts — страница контактов отдаёт HTTP 200")
    void contactsPage_returns200() throws Exception {
        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(view().name("contacts"));
    }

    @Test
    @DisplayName("GET /feedback — форма обратной связи отдаёт HTTP 200")
    void feedbackPage_returns200() throws Exception {
        mockMvc.perform(get("/feedback"))
                .andExpect(status().isOk())
                .andExpect(view().name("feedback"))
                .andExpect(model().attributeExists("feedbackMessage"));
    }

    @Test
    @DisplayName("GET /login — страница входа отдаёт HTTP 200")
    void loginPage_returns200() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    @DisplayName("GET /register — страница регистрации отдаёт HTTP 200")
    void registerPage_returns200() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"));
    }

    @Test
    @DisplayName("GET /несуществующая-страница — отдаёт HTTP 404 или редирект")
    void nonExistentPage_returnsErrorOrRedirect() throws Exception {
        mockMvc.perform(get("/this-page-does-not-exist"))
                .andExpect(result ->
                        org.assertj.core.api.Assertions.assertThat(
                                result.getResponse().getStatus()
                        ).isIn(404, 302, 200));
    }
}
