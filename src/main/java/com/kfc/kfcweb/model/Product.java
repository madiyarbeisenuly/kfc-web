package com.kfc.kfcweb.model;

import jakarta.persistence.*;

/**
 * Продукт — наследует BaseEntity (OOP: Наследование + Полиморфизм)
 */
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;
    private String category;
    private String description;
    private String imageUrl;
    private boolean available = true;

    public Product() {}

    @Override
    public String getSummary() {
        return "Продукт: " + name + " | Цена: " + price + " ₸ | Категория: " + category;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    public double getPrice() { return price; }
    public void setPrice(double p) { this.price = p; }
    public String getCategory() { return category; }
    public void setCategory(String c) { this.category = c; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String u) { this.imageUrl = u; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean a) { this.available = a; }
}
