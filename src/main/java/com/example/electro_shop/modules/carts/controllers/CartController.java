package com.example.electro_shop.modules.carts.controllers;

import com.example.electro_shop.modules.user.model.User;
import com.example.electro_shop.modules.carts.dtos.CartItemRequest;
import com.example.electro_shop.modules.carts.dtos.CartResponse;
import com.example.electro_shop.modules.carts.mappers.CartMapper;
import com.example.electro_shop.modules.carts.models.Cart;
import com.example.electro_shop.modules.carts.services.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;

    public CartController(CartService cartService, CartMapper cartMapper) {
        this.cartService = cartService;
        this.cartMapper = cartMapper;
    }

    @GetMapping
    public ResponseEntity<CartResponse> getMyCart(@AuthenticationPrincipal User user) {
        Cart cart = cartService.getOrCreateCart(user);
        return ResponseEntity.ok(cartMapper.toResponseDto(cart));
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addItem(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CartItemRequest request) {
        Cart cart = cartService.addItemToCart(user, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(cartMapper.toResponseDto(cart));
    }

    @PostMapping("/sync")
    public ResponseEntity<CartResponse> syncCart(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody List<CartItemRequest> items) {
        Cart cart = cartService.syncCart(user, items);
        return ResponseEntity.ok(cartMapper.toResponseDto(cart));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal User user) {
        cartService.clearCart(user);
        return ResponseEntity.noContent().build();
    }
}
