package com.kfc.kfcweb.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Сервис корзины — сериализация через ObjectOutputStream (критерий: Serialization)
 */
@Service
public class CartService {

    private static final Logger log = Logger.getLogger(CartService.class.getName());
    private static final String CART_FILE = "cart.ser";

    /**
     * Сохранить корзину в файл (ObjectOutputStream — критерий: Serialization + File I/O)
     */
    public void saveCart(Map<Long, Integer> cart) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CART_FILE))) {
            oos.writeObject(cart);
            log.info("[Serialization] Корзина сохранена: " + cart.size() + " позиций");
        } catch (IOException e) {
            log.warning("[Serialization] Ошибка сохранения корзины: " + e.getMessage());
        }
    }

    /**
     * Загрузить корзину из файла (ObjectInputStream — критерий: Serialization + File I/O)
     */
    @SuppressWarnings("unchecked")
    public Map<Long, Integer> loadCart() {
        File file = new File(CART_FILE);
        if (!file.exists()) return new HashMap<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CART_FILE))) {
            Map<Long, Integer> cart = (Map<Long, Integer>) ois.readObject();
            log.info("[Serialization] Корзина загружена: " + cart.size() + " позиций");
            return cart;
        } catch (IOException | ClassNotFoundException e) {
            log.warning("[Serialization] Ошибка загрузки корзины: " + e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Очистить файл корзины
     */
    public void clearCart() {
        File file = new File(CART_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
