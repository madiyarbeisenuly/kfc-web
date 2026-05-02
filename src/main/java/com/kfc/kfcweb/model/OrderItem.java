package com.kfc.kfcweb.model;

import jakarta.persistence.*;

/**
 * Позиция заказа — наследует BaseEntity (OOP: Наследование)
 */
@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private String productName;
    private Integer quantity;
    private Double price;

    public OrderItem() {}

    @Override
    public String getSummary() {
        return productName + " x" + quantity + " = " + (price * quantity) + " ₸";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long o) { this.orderId = o; }
    public String getProductName() { return productName; }
    public void setProductName(String n) { this.productName = n; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer q) { this.quantity = q; }
    public Double getPrice() { return price; }
    public void setPrice(Double p) { this.price = p; }
}
