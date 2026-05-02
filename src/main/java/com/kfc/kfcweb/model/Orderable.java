package com.kfc.kfcweb.model;

/**
 * Интерфейс для объектов, которые можно заказать (OOP: Интерфейсы)
 */
public interface Orderable {
    double calculateTotal();
    String getOrderSummary();
    boolean isAvailable();
}
