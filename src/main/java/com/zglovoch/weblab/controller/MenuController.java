package com.zglovoch.weblab.controller;

import com.zglovoch.weblab.model.MenuItem;
import com.zglovoch.weblab.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public String menu(Model model) {
        model.addAttribute("currentPage", "menu");
        List<String> categories = menuService.findDistinctCategories();
        Map<String, List<MenuItem>> menuByCategory = new LinkedHashMap<>();
        for (String cat : categories) {
            List<MenuItem> items = menuService.findByCategory(cat);
            if (!items.isEmpty()) {
                menuByCategory.put(cat, items);
            }
        }
        model.addAttribute("menuByCategory", menuByCategory);
        model.addAttribute("categories", categories);
        return "menu";
    }
}
