package com.example.electro_shop.modules.carts.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CartItemRequest {

    @NotNull
    private UUID productId;

    @NotNull
    @Min(1)
    private Integer quantity;

    public CartItemRequest() {
    }

    public CartItemRequest(UUID productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public @NotNull UUID getProductId() {
        return productId;
    }

    public void setProductId(@NotNull UUID productId) {
        this.productId = productId;
    }

    public @NotNull @Min(1) Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(@NotNull @Min(1) Integer quantity) {
        this.quantity = quantity;
    }
}
