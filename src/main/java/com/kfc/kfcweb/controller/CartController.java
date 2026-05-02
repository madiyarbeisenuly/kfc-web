package com.kfc.kfcweb.controller;

import com.kfc.kfcweb.model.Order;
import com.kfc.kfcweb.model.OrderItem;
import com.kfc.kfcweb.model.Product;
import com.kfc.kfcweb.service.CartService;
import com.kfc.kfcweb.service.OrderService;
import com.kfc.kfcweb.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Контроллер корзины — использует коллекции и сериализацию (критерии: Collections + Serialization)
 */
@Controller
@SessionAttributes("cart")
public class CartController {

    @Autowired private ProductService productService;
    @Autowired private OrderService orderService;
    @Autowired private CartService cartService;
    @Autowired private SimpMessagingTemplate messagingTemplate;

    @ModelAttribute("cart")
    public Map<Long, Integer> initCart() {
        return cartService.loadCart();
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            @ModelAttribute("cart") Map<Long, Integer> cart) {
        cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
        cartService.saveCart(cart);
        return "redirect:/menu";
    }

    @PostMapping("/add-to-cart-detail")
    public String addToCartDetail(@RequestParam Long productId,
                                  @RequestParam int quantity,
                                  @ModelAttribute("cart") Map<Long, Integer> cart) {
        cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
        cartService.saveCart(cart);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long productId,
                                 @ModelAttribute("cart") Map<Long, Integer> cart) {
        cart.remove(productId);
        cartService.saveCart(cart);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String viewCart(Model model,
                           @ModelAttribute("cart") Map<Long, Integer> cart,
                           HttpSession session) {
        // LinkedHashMap сохраняет порядок (Collections)
        Map<Product, Integer> cartItems = new LinkedHashMap<>();
        double total = 0;

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product p = productService.getProductById(entry.getKey());
            if (p != null) {
                cartItems.put(p, entry.getValue());
                total += p.getPrice() * entry.getValue();
            }
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("cartCount", cart.values().stream().mapToInt(Integer::intValue).sum());
        model.addAttribute("username", session.getAttribute("username"));
        return "cart";
    }

    @PostMapping("/checkout")
    public String checkout(@ModelAttribute("cart") Map<Long, Integer> cart,
                           HttpSession session,
                           Model model) {
        if (cart.isEmpty()) return "redirect:/cart";

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        Order order = new Order();
        order.setUserId(userId);

        // ArrayList для позиций заказа (Collections)
        List<OrderItem> items = new ArrayList<>();
        double total = 0;

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product p = productService.getProductById(entry.getKey());
            if (p == null) continue;
            OrderItem item = new OrderItem();
            item.setProductName(p.getName());
            item.setQuantity(entry.getValue());
            item.setPrice(p.getPrice());
            items.add(item);
            total += p.getPrice() * entry.getValue();
        }

        order.setTotal(total);
        order.setItems(items);

        // Асинхронное сохранение заказа (Threads + Async)
        orderService.saveOrderAsync(order);

        // WebSocket уведомление (Socket programming)
        messagingTemplate.convertAndSend("/topic/status",
                "✅ Ваш заказ принят! Готовим для вас...");

        cart.clear();
        cartService.saveCart(cart);
        return "redirect:/cart?success";
    }

    @GetMapping("/my-orders")
    public String myOrders(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";

        model.addAttribute("orders", orderService.getUserOrders(userId));
        model.addAttribute("username", session.getAttribute("username"));
        return "my-orders";
    }
}
