package com.example.electro_shop.modules.orders.repositories;

import com.example.electro_shop.modules.auth.models.User;
import com.example.electro_shop.modules.orders.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderId(UUID orderId);

    List<Order> findByUserOrderByOrderDateDesc(User user);

}
