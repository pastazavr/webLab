package com.zglovoch.weblab.controller.admin;

import com.zglovoch.weblab.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/feedback")
@RequiredArgsConstructor
public class AdminFeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("messages", feedbackService.findAll());
        model.addAttribute("unreadCount", feedbackService.countUnread());
        return "admin/feedback/list";
    }

    @PostMapping("/{id}/read")
    public String markRead(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        feedbackService.markAsRead(id);
        redirectAttrs.addFlashAttribute("success", "Сообщение отмечено как прочитанное.");
        return "redirect:/admin/feedback";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        feedbackService.deleteById(id);
        redirectAttrs.addFlashAttribute("success", "Сообщение удалено.");
        return "redirect:/admin/feedback";
    }
}
