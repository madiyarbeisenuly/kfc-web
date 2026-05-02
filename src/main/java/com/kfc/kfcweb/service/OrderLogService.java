package com.kfc.kfcweb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Сервис логирования заказов через чистый JDBC (критерий: JDBC + MySQL)
 * Использует DriverManager напрямую, без JPA
 */
@Service
public class OrderLogService {

    private static final Logger log = Logger.getLogger(OrderLogService.class.getName());

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    /**
     * Создаём таблицу логов, если её нет (вызывается при старте)
     */
    public void initTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS order_logs (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    order_id BIGINT,
                    user_id BIGINT,
                    total DOUBLE,
                    status VARCHAR(50),
                    logged_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    message TEXT
                )
                """;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            log.info("[JDBC] Таблица order_logs готова");
        } catch (SQLException e) {
            log.warning("[JDBC] Ошибка создания таблицы: " + e.getMessage());
        }
    }

    /**
     * Записать лог заказа через PreparedStatement (JDBC)
     */
    public void logOrder(Long orderId, Long userId, double total, String status, String message) {
        String sql = "INSERT INTO order_logs (order_id, user_id, total, status, logged_at, message) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId != null ? orderId : 0);
            ps.setLong(2, userId != null ? userId : 0);
            ps.setDouble(3, total);
            ps.setString(4, status);
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(6, message);
            ps.executeUpdate();
            log.info("[JDBC] Лог заказа записан: orderId=" + orderId);
        } catch (SQLException e) {
            log.warning("[JDBC] Ошибка записи лога: " + e.getMessage());
        }
    }

    /**
     * Получить все логи через ResultSet (JDBC)
     */
    public List<Map<String, Object>> getAllLogs() {
        List<Map<String, Object>> logs = new ArrayList<>();
        String sql = "SELECT * FROM order_logs ORDER BY logged_at DESC LIMIT 100";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("id", rs.getLong("id"));
                row.put("orderId", rs.getLong("order_id"));
                row.put("userId", rs.getLong("user_id"));
                row.put("total", rs.getDouble("total"));
                row.put("status", rs.getString("status"));
                row.put("loggedAt", rs.getTimestamp("logged_at"));
                row.put("message", rs.getString("message"));
                logs.add(row);
            }
        } catch (SQLException e) {
            log.warning("[JDBC] Ошибка чтения логов: " + e.getMessage());
        }
        return logs;
    }
}
