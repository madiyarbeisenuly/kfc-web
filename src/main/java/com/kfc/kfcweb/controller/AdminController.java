package com.kfc.kfcweb.controller;

import com.kfc.kfcweb.model.Product;
import com.kfc.kfcweb.service.OrderLogService;
import com.kfc.kfcweb.service.ProductService;
import com.kfc.kfcweb.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UserService userService;
    @Autowired private ProductService productService;
    @Autowired private OrderLogService orderLogService;

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("role"));
    }

    @GetMapping
    public String adminPanel(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/";
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("logs", orderLogService.getAllLogs());
        model.addAttribute("username", session.getAttribute("username"));
        return "admin";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/";
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/products/add")
    public String addProduct(@RequestParam String name,
                              @RequestParam double price,
                              @RequestParam String category,
                              @RequestParam String description,
                              @RequestParam(required = false) String imageUrl,
                              HttpSession session) {
        if (!isAdmin(session)) return "redirect:/";
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setCategory(category);
        p.setDescription(description);
        p.setImageUrl(imageUrl != null ? imageUrl : "placeholder.jpg");
        p.setAvailable(true);
        productService.saveProduct(p);
        return "redirect:/admin";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/";
        productService.deleteProduct(id);
        return "redirect:/admin";
    }
}
