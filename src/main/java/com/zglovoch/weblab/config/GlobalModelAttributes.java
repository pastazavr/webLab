package com.zglovoch.weblab.config;

import com.zglovoch.weblab.model.DynamicPage;
import com.zglovoch.weblab.service.DynamicPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * Adds auth info and dynamic pages to every model — templates
 * use ${isAuthenticated}, ${isAdmin}, ${currentUser}, ${dynamicPages}.
 * dynamicPages() uses DynamicPageService.findVisible() which is @Cacheable.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final DynamicPageService dynamicPageService;

    @ModelAttribute("isAuthenticated")
    public boolean isAuthenticated(Authentication auth) {
        return auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getName());
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication auth) {
        if (auth == null) return false;
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @ModelAttribute("currentUser")
    public String currentUser(Authentication auth) {
        return (auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getName()))
                ? auth.getName() : null;
    }

    @ModelAttribute("dynamicPages")
    public List<DynamicPage> dynamicPages() {
        return dynamicPageService.findVisible(); // результат берётся из @Cacheable кэша
    }
}
