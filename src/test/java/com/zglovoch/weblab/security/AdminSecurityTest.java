package com.zglovoch.weblab.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тесты Spring Security — проверка разграничения прав доступа.
 * MockMvc строится вручную через webAppContextSetup()
 */
@SpringBootTest
class AdminSecurityTest {

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

    // ---------- Неаутентифицированный пользователь ----------

    @Test
    @DisplayName("GET /admin без авторизации — редирект на /login (302)")
    void admin_unauthenticated_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @DisplayName("GET /admin/menu без авторизации — редирект на /login")
    void adminMenu_unauthenticated_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/admin/menu"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("GET /admin/gallery без авторизации — редирект на /login")
    void adminGallery_unauthenticated_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/admin/gallery"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("GET /admin/feedback без авторизации — редирект на /login")
    void adminFeedback_unauthenticated_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/admin/feedback"))
                .andExpect(status().is3xxRedirection());
    }

    // ---------- Обычный пользователь (ROLE_USER) ----------

    @Test
    @DisplayName("GET /admin с правами USER — HTTP 403 Forbidden")
    @WithMockUser(roles = "USER")
    void admin_withUserRole_returns403() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /admin/pages с правами USER — HTTP 403 Forbidden")
    @WithMockUser(roles = "USER")
    void adminPages_withUserRole_returns403() throws Exception {
        mockMvc.perform(get("/admin/pages"))
                .andExpect(status().isForbidden());
    }

    // ---------- Администратор (ROLE_ADMIN) ----------

    @Test
    @DisplayName("GET /admin с правами ADMIN — HTTP 200 OK")
    @WithMockUser(roles = "ADMIN")
    void admin_withAdminRole_returns200() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /admin/menu с правами ADMIN — HTTP 200 OK")
    @WithMockUser(roles = "ADMIN")
    void adminMenu_withAdminRole_returns200() throws Exception {
        mockMvc.perform(get("/admin/menu"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /admin/gallery с правами ADMIN — HTTP 200 OK")
    @WithMockUser(roles = "ADMIN")
    void adminGallery_withAdminRole_returns200() throws Exception {
        mockMvc.perform(get("/admin/gallery"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /admin/pages с правами ADMIN — HTTP 200 OK")
    @WithMockUser(roles = "ADMIN")
    void adminPages_withAdminRole_returns200() throws Exception {
        mockMvc.perform(get("/admin/pages"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /admin/feedback с правами ADMIN — HTTP 200 OK")
    @WithMockUser(roles = "ADMIN")
    void adminFeedback_withAdminRole_returns200() throws Exception {
        mockMvc.perform(get("/admin/feedback"))
                .andExpect(status().isOk());
    }

    // ---------- Публичные страницы доступны всем ----------

    @Test
    @DisplayName("GET / без авторизации — HTTP 200 OK (публичная страница)")
    void homePage_unauthenticated_returns200() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /menu без авторизации — HTTP 200 OK")
    void menuPage_unauthenticated_returns200() throws Exception {
        mockMvc.perform(get("/menu"))
                .andExpect(status().isOk());
    }
}
