package com.example.electro_shop.modules.catalog.repositories;

import com.example.electro_shop.modules.catalog.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductId(UUID id);
}
