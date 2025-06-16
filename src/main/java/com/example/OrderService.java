package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@RestController
@SpringBootApplication
public class OrderService {

    private final Map<String, String> orders = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(OrderService.class, args);
    }

    @PostMapping("/orders")
    public ResponseEntity<String> processOrder(@RequestBody Map<String, Object> order) {
        String orderId = "ORD-" + System.currentTimeMillis();
        orders.put(orderId, order.toString());

        // Simulate processing time
        try { Thread.sleep(100); } catch (InterruptedException e) {}

        return ResponseEntity.ok("Order " + orderId + " processed. Total orders: " + orders.size());
    }

    @GetMapping("/orders/count")
    public ResponseEntity<Map<String, Integer>> getOrderCount() {
        return ResponseEntity.ok(Map.of("totalOrders", orders.size()));
    }

    @GetMapping("/memory")
    public ResponseEntity<Map<String, String>> getMemoryInfo() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        return ResponseEntity.ok(Map.of(
                "maxMemory", formatBytes(maxMemory),
                "totalMemory", formatBytes(totalMemory),
                "usedMemory", formatBytes(totalMemory - freeMemory),
                "freeMemory", formatBytes(freeMemory)
        ));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Service healthy");
    }

    private String formatBytes(long bytes) {
        return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
    }
}