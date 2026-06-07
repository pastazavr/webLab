package com.zglovoch.weblab.controller.admin;

import com.zglovoch.weblab.model.GalleryItem;
import com.zglovoch.weblab.service.GalleryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/gallery")
@RequiredArgsConstructor
public class AdminGalleryController {

    private final GalleryService galleryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", galleryService.findAll());
        return "admin/gallery/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("item", new GalleryItem());
        model.addAttribute("isNew", true);
        return "admin/gallery/edit";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("item") GalleryItem item,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("isNew", true);
            return "admin/gallery/edit";
        }
        galleryService.save(item);
        redirectAttrs.addFlashAttribute("success", "Фото «" + item.getTitle() + "» добавлено.");
        return "redirect:/admin/gallery";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        GalleryItem item = galleryService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Фото не найдено: " + id));
        model.addAttribute("item", item);
        model.addAttribute("isNew", false);
        return "admin/gallery/edit";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("item") GalleryItem item,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("isNew", false);
            return "admin/gallery/edit";
        }
        item.setId(id);
        galleryService.save(item);
        redirectAttrs.addFlashAttribute("success", "Фото обновлено.");
        return "redirect:/admin/gallery";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        galleryService.deleteById(id);
        redirectAttrs.addFlashAttribute("success", "Фото удалено.");
        return "redirect:/admin/gallery";
    }
}
