package com.zglovoch.weblab.controller;

import com.zglovoch.weblab.dto.UserRegistrationDto;
import com.zglovoch.weblab.model.User;
import com.zglovoch.weblab.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMsg", "Неверное имя пользователя или пароль.");
        }
        if (logout != null) {
            model.addAttribute("successMsg", "Вы успешно вышли из системы.");
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userDto") UserRegistrationDto dto,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttrs) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        if (!dto.passwordsMatch()) {
            model.addAttribute("errorMsg", "Пароли не совпадают.");
            return "auth/register";
        }
        if (userService.existsByUsername(dto.getUsername())) {
            model.addAttribute("errorMsg", "Пользователь с таким именем уже существует.");
            return "auth/register";
        }
        userService.register(dto.getUsername(), dto.getPassword(), User.Role.USER);
        redirectAttrs.addFlashAttribute("successMsg", "Регистрация прошла успешно! Войдите в систему.");
        return "redirect:/login";
    }
}
