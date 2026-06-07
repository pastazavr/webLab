package com.zglovoch.weblab.controller.admin;

import com.zglovoch.weblab.model.DynamicPage;
import com.zglovoch.weblab.service.DynamicPageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/pages")
@RequiredArgsConstructor
public class AdminPageController {

    private final DynamicPageService pageService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pages", pageService.findAll());
        return "admin/pages/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("page", new DynamicPage());
        model.addAttribute("isNew", true);
        return "admin/pages/edit";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("page") DynamicPage page,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttrs) {
        if (!result.hasErrors() && pageService.slugExists(page.getSlug())) {
            result.rejectValue("slug", "duplicate", "Страница с таким slug уже существует");
        }
        if (result.hasErrors()) {
            model.addAttribute("isNew", true);
            return "admin/pages/edit";
        }
        pageService.save(page);
        redirectAttrs.addFlashAttribute("success", "Страница «" + page.getTitle() + "» создана.");
        return "redirect:/admin/pages";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        DynamicPage page = pageService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Страница не найдена: " + id));
        model.addAttribute("page", page);
        model.addAttribute("isNew", false);
        return "admin/pages/edit";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("page") DynamicPage page,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttrs) {
        if (!result.hasErrors() && pageService.slugExistsExcept(page.getSlug(), id)) {
            result.rejectValue("slug", "duplicate", "Страница с таким slug уже существует");
        }
        if (result.hasErrors()) {
            model.addAttribute("isNew", false);
            return "admin/pages/edit";
        }
        page.setId(id);
        pageService.save(page);
        redirectAttrs.addFlashAttribute("success", "Страница обновлена.");
        return "redirect:/admin/pages";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        pageService.deleteById(id);
        redirectAttrs.addFlashAttribute("success", "Страница удалена.");
        return "redirect:/admin/pages";
    }
}
