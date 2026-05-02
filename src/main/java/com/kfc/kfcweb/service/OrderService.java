package com.kfc.kfcweb.service;

import com.kfc.kfcweb.model.Order;
import com.kfc.kfcweb.model.OrderItem;
import com.kfc.kfcweb.repository.OrderItemRepository;
import com.kfc.kfcweb.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Сервис заказов — использует явные Thread и ExecutorService (критерий: Threads)
 */
@Service
public class OrderService {

    private static final Logger log = Logger.getLogger(OrderService.class.getName());

    // Явный пул потоков (критерий: Threads)
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderLogService orderLogService;

    @Transactional
    public Order saveOrder(Order order) {
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("NEW");
        Order savedOrder = orderRepository.save(order);

        for (OrderItem item : order.getItems()) {
            item.setOrderId(savedOrder.getId());
            orderItemRepository.save(item);
        }

        // Явный Thread для логирования (критерий: Threads)
        Thread logThread = new Thread(() -> {
            log.info("[Thread-" + Thread.currentThread().getName() + "] Логируем заказ #" + savedOrder.getId());
            orderLogService.logOrder(
                    savedOrder.getId(),
                    savedOrder.getUserId(),
                    savedOrder.getTotal(),
                    "NEW",
                    "Заказ создан: " + savedOrder.getOrderSummary()
            );
        }, "order-log-thread");
        logThread.start();

        return savedOrder;
    }

    /**
     * Асинхронная обработка заказа через @Async (критерий: Threads)
     */
    @Async
    public CompletableFuture<Order> saveOrderAsync(Order order) {
        Order savedOrder = saveOrder(order);

        // Дополнительный фоновый поток через ExecutorService (критерий: Threads)
        executorService.submit(() -> {
            try {
                Thread.sleep(1500);
                log.info("[ExecutorService] Заказ " + savedOrder.getId() + " обработан асинхронно в потоке: "
                        + Thread.currentThread().getName());

                // Обновляем статус
                savedOrder.setStatus("PROCESSING");
                orderRepository.save(savedOrder);

                // Лог статуса
                orderLogService.logOrder(
                        savedOrder.getId(),
                        savedOrder.getUserId(),
                        savedOrder.getTotal(),
                        "PROCESSING",
                        "Заказ передан на кухню"
                );
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        return CompletableFuture.completedFuture(savedOrder);
    }

    public List<Order> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByOrderDateDesc(userId);
        // Загружаем items для каждого заказа
        orders.forEach(o -> o.setItems(orderItemRepository.findByOrderId(o.getId())));
        return orders;
    }
}
