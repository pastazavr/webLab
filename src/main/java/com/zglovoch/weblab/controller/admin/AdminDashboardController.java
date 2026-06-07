package com.zglovoch.weblab.controller.admin;

import com.zglovoch.weblab.service.DynamicPageService;
import com.zglovoch.weblab.service.FeedbackService;
import com.zglovoch.weblab.service.GalleryService;
import com.zglovoch.weblab.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final MenuService menuService;
    private final GalleryService galleryService;
    private final DynamicPageService dynamicPageService;
    private final FeedbackService feedbackService;

    @GetMapping({"", "/"})
    public String dashboard(Model model) {
        model.addAttribute("menuCount", menuService.count());
        model.addAttribute("galleryCount", galleryService.count());
        model.addAttribute("pageCount", dynamicPageService.count());
        model.addAttribute("unreadFeedback", feedbackService.countUnread());
        model.addAttribute("recentFeedback", feedbackService.findAll().stream().limit(5).toList());
        return "admin/dashboard";
    }
}
