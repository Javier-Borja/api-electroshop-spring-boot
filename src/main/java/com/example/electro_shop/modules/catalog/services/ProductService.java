package com.example.electro_shop.modules.catalog.services;

import com.example.electro_shop.modules.catalog.dtos.ProductCardDto;
import com.example.electro_shop.modules.catalog.dtos.ProductDetailDto;
import com.example.electro_shop.modules.catalog.mappers.ProductMapper;
import com.example.electro_shop.modules.catalog.models.Product;
import com.example.electro_shop.modules.catalog.repositories.ProductRepository;
import com.example.electro_shop.shared.dto.PageResponse;
import com.example.electro_shop.shared.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public PageResponse<ProductCardDto> getAllProducts(Pageable pageable) {
        Page<ProductCardDto> products = productRepository.findAll(pageable)
                .map(productMapper::toProductCardDto);

        return new PageResponse<>(products);
    }

    public ProductDetailDto getProductById(UUID id) {
        return productRepository.findByProductId(id)
                .map(productMapper::toProductDetailDto)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado."));
    }

}
