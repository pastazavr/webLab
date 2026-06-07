package com.zglovoch.weblab.controller;

import com.zglovoch.weblab.service.GalleryService;
import com.zglovoch.weblab.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MenuService menuService;
    private final GalleryService galleryService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("currentPage", "home");
        // Show first 3 available menu items as featured
        model.addAttribute("featuredItems", menuService.findAll().stream()
                .filter(com.zglovoch.weblab.model.MenuItem::isAvailable)
                .limit(3).toList());
        // Show first 6 gallery items as preview
        model.addAttribute("galleryPreview", galleryService.findAll().stream()
                .limit(6).toList());
        return "index";
    }
}
