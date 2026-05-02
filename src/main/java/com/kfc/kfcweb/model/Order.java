package com.kfc.kfcweb.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Заказ — наследует BaseEntity, реализует Orderable
 * (OOP: Наследование + Полиморфизм + Интерфейсы)
 */
@Entity
@Table(name = "orders")
public class Order extends BaseEntity implements Orderable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private Double total;
    private String status;

    @Transient
    private List<OrderItem> items = new ArrayList<>();

    public Order() {}

    // --- Реализация Orderable ---
    @Override
    public double calculateTotal() {
        if (items == null || items.isEmpty()) return total != null ? total : 0;
        return items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
    }

    @Override
    public String getOrderSummary() {
        return "Заказ #" + id + " | Статус: " + status + " | Сумма: " + total + " ₸";
    }

    @Override
    public boolean isAvailable() {
        return !"CANCELLED".equals(status);
    }

    // --- Полиморфный метод из BaseEntity ---
    @Override
    public String getSummary() { return getOrderSummary(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long u) { this.userId = u; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime d) { this.orderDate = d; }
    public Double getTotal() { return total; }
    public void setTotal(Double t) { this.total = t; }
    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
