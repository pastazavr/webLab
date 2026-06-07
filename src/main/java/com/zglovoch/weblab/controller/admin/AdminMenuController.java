package com.zglovoch.weblab.controller.admin;

import com.zglovoch.weblab.model.MenuItem;
import com.zglovoch.weblab.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/menu")
@RequiredArgsConstructor
public class AdminMenuController {

    private final MenuService menuService;

    private static final List<String> CATEGORIES =
            List.of("Напитки", "Завтраки", "Основные блюда", "Десерты");

    @GetMapping
    public String list(Model model) {
        model.addAttribute("items", menuService.findAll());
        return "admin/menu/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("item", new MenuItem());
        model.addAttribute("isNew", true);
        model.addAttribute("categories", CATEGORIES);
        return "admin/menu/edit";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("item") MenuItem item,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("isNew", true);
            model.addAttribute("categories", CATEGORIES);
            return "admin/menu/edit";
        }
        menuService.save(item);
        redirectAttrs.addFlashAttribute("success", "Позиция «" + item.getName() + "» добавлена.");
        return "redirect:/admin/menu";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        MenuItem item = menuService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Позиция не найдена: " + id));
        model.addAttribute("item", item);
        model.addAttribute("isNew", false);
        model.addAttribute("categories", CATEGORIES);
        return "admin/menu/edit";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("item") MenuItem item,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("isNew", false);
            model.addAttribute("categories", CATEGORIES);
            return "admin/menu/edit";
        }
        item.setId(id);
        menuService.save(item);
        redirectAttrs.addFlashAttribute("success", "Позиция обновлена.");
        return "redirect:/admin/menu";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        menuService.deleteById(id);
        redirectAttrs.addFlashAttribute("success", "Позиция удалена.");
        return "redirect:/admin/menu";
    }
}
