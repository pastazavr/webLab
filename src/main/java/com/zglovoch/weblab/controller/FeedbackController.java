package com.zglovoch.weblab.controller;

import com.zglovoch.weblab.model.FeedbackMessage;
import com.zglovoch.weblab.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping
    public String form(Model model) {
        model.addAttribute("currentPage", "feedback");
        if (!model.containsAttribute("feedbackMessage")) {
            model.addAttribute("feedbackMessage", new FeedbackMessage());
        }
        return "feedback";
    }

    @PostMapping
    public String submit(@Valid @ModelAttribute("feedbackMessage") FeedbackMessage message,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            model.addAttribute("currentPage", "feedback");
            return "feedback";
        }
        feedbackService.save(message);
        redirectAttrs.addFlashAttribute("success",
                "Спасибо! Ваше сообщение принято. Мы свяжемся с вами в ближайшее время.");
        return "redirect:/feedback";
    }
}
