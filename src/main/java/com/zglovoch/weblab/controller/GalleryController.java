package com.zglovoch.weblab.controller;

import com.zglovoch.weblab.service.GalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gallery")
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;

    @GetMapping
    public String gallery(Model model) {
        model.addAttribute("currentPage", "gallery");
        model.addAttribute("galleryItems", galleryService.findAll());
        return "gallery";
    }
}
