package com.zglovoch.weblab.controller;

import com.zglovoch.weblab.model.DynamicPage;
import com.zglovoch.weblab.service.DynamicPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/pages")
@RequiredArgsConstructor
public class DynamicPageController {

    private final DynamicPageService dynamicPageService;

    @GetMapping("/{slug}")
    public String showPage(@PathVariable String slug, Model model) {
        DynamicPage page = dynamicPageService.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Страница не найдена"));

        if (!page.isVisible()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Страница не найдена");
        }

        model.addAttribute("page", page);
        return "pages/show";
    }
}
