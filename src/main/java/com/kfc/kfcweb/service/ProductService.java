package com.kfc.kfcweb.service;

import com.kfc.kfcweb.model.Product;
import com.kfc.kfcweb.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Сервис продуктов — использует коллекции (критерий: Collections)
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    /**
     * Сгруппировать продукты по категориям — Map<String, List<Product>> (критерий: Collections)
     */
    public Map<String, List<Product>> getProductsByCategory() {
        List<Product> all = productRepository.findAll();
        Map<String, List<Product>> grouped = new LinkedHashMap<>();

        // Заданный порядок категорий
        List<String> categoryOrder = Arrays.asList(
                "chicken", "basket", "box", "burger", "twister", "snack", "sauce"
        );

        for (String cat : categoryOrder) {
            grouped.put(cat, new ArrayList<>());
        }

        for (Product p : all) {
            grouped.computeIfAbsent(p.getCategory(), k -> new ArrayList<>()).add(p);
        }

        return grouped;
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
