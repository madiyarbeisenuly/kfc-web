package com.kfc.kfcweb.controller;

import com.kfc.kfcweb.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        model.addAttribute("username", session.getAttribute("username"));
        return "index";
    }

    @GetMapping("/menu")
    public String menu(Model model, HttpSession session) {
        model.addAttribute("productsByCategory", productService.getProductsByCategory());
        model.addAttribute("username", session.getAttribute("username"));
        return "menu";
    }

    @GetMapping("/ingredients")
    public String ingredients(HttpSession session, Model model) {
        model.addAttribute("username", session.getAttribute("username"));
        return "ingredients";
    }
}
