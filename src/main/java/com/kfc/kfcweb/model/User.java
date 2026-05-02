package com.kfc.kfcweb.model;

import jakarta.persistence.*;

/**
 * Пользователь — наследует BaseEntity (OOP: Наследование + Полиморфизм)
 */
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role = "USER";

    public User() {}

    @Override
    public String getSummary() {
        return "Пользователь: " + username + " | Email: " + email + " | Роль: " + role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; }
    public String getPassword() { return password; }
    public void setPassword(String p) { this.password = p; }
    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e; }
    public String getRole() { return role; }
    public void setRole(String r) { this.role = r; }
}
