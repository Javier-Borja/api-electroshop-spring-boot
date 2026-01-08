package com.example.electro_shop.modules.carts.services;

import com.example.electro_shop.modules.user.model.User;
import com.example.electro_shop.modules.carts.dtos.CartItemRequest;
import com.example.electro_shop.modules.carts.models.Cart;
import com.example.electro_shop.modules.carts.models.CartItem;
import com.example.electro_shop.modules.carts.repositories.CartRepository;
import com.example.electro_shop.modules.catalog.models.Product;
import com.example.electro_shop.modules.catalog.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public Cart addItemToCart(User user, UUID productId, Integer quantity) {
        Cart cart = getOrCreateCart(user);
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        updateOrCreateItem(cart, product, quantity);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart syncCart(User user, List<CartItemRequest> itemsFromFront) {
        Cart cart = getOrCreateCart(user);
        cart.getItems().clear();
        cartRepository.saveAndFlush(cart);
        
        for (CartItemRequest itemDto : itemsFromFront) {
            productRepository.findByProductId(itemDto.getProductId()).ifPresent(product -> {
                CartItem newItem = new CartItem();
                newItem.setCart(cart);
                newItem.setProduct(product);
                newItem.setQuantity(itemDto.getQuantity());
                cart.getItems().add(newItem);
            });
        }

        return cartRepository.save(cart);
    }

    private void updateOrCreateItem(Cart cart, Product product, Integer quantity) {
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }
    }

    @Transactional
    public void clearCart(User user) {
        cartRepository.findByUser(user).ifPresent(cart -> {
            cart.getItems().clear();
            cartRepository.save(cart);
        });
    }
}
