package com.kfc.kfcweb.controller;

import com.kfc.kfcweb.model.User;
import com.kfc.kfcweb.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginForm(HttpSession session) {
        if (session.getAttribute("userId") != null) return "redirect:/";
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        Optional<User> user = userService.login(username, password);
        if (user.isPresent()) {
            session.setAttribute("userId", user.get().getId());
            session.setAttribute("username", user.get().getUsername());
            session.setAttribute("role", user.get().getRole());
            return "redirect:/";
        }
        model.addAttribute("error", "Неверный логин или пароль");
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(HttpSession session) {
        if (session.getAttribute("userId") != null) return "redirect:/";
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String email,
                           Model model) {
        if (userService.register(username, password, email)) {
            return "redirect:/login?registered";
        }
        model.addAttribute("error", "Пользователь уже существует");
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        User user = userService.getAllUsers().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst().orElse(null);
        model.addAttribute("user", user);
        model.addAttribute("username", session.getAttribute("username"));
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String email,
                                HttpSession session) {
        // обновление профиля
        return "redirect:/profile?updated";
    }

    @PostMapping("/profile/delete")
    public String deleteProfile(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) userService.deleteUser(userId);
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        Optional<User> userOpt = userService.getAllUsers().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst();

        if (userOpt.isEmpty()) return "redirect:/login";

        User user = userOpt.get();

        if (!user.getPassword().equals(oldPassword)) {
            model.addAttribute("error", "Старый пароль неверный");
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("user", user);
            return "profile";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("user", user);
            return "profile";
        }

        user.setPassword(newPassword);
        userService.saveUser(user);

        return "redirect:/profile?passwordChanged";
    }
}

