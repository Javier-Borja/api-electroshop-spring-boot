package com.example.electro_shop.modules.carts.repositories;

import com.example.electro_shop.modules.user.model.User;
import com.example.electro_shop.modules.carts.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
}
