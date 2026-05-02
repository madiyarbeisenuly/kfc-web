package com.kfc.kfcweb.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Базовый абстрактный класс для всех сущностей (OOP: Абстракция + Наследование)
 */
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    protected LocalDateTime createdAt;

    public BaseEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }

    // Абстрактный метод — каждый потомок обязан реализовать (OOP: Полиморфизм)
    public abstract String getSummary();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[createdAt=" + createdAt + "]";
    }
}
