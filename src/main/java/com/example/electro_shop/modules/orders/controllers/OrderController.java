package com.example.electro_shop.modules.orders.controllers;

import com.example.electro_shop.modules.user.model.User;
import com.example.electro_shop.modules.orders.dtos.CheckoutRequest;
import com.example.electro_shop.modules.orders.dtos.OrderResponse;
import com.example.electro_shop.modules.orders.dtos.OrderSummary;
import com.example.electro_shop.modules.orders.dtos.StripeResponse;
import com.example.electro_shop.modules.orders.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkout(
            @AuthenticationPrincipal User user,
            @RequestBody CheckoutRequest request) throws Exception {
        return ResponseEntity.ok(new StripeResponse(orderService.createCheckoutSession(user, request)));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrderDetails(orderId));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> stripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            orderService.handleWebhook(payload, sigHeader);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderSummary>> getMyOrders() {
        return ResponseEntity.ok(orderService.getMyOrders());
    }
}
