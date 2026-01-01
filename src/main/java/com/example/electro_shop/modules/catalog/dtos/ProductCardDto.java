package com.example.electro_shop.modules.catalog.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductCardDto {

    private UUID productId;
    private String name;
    private String slug;
    private BigDecimal price;
    private String imageUrl;
    private String brand;
    private String category;

    public ProductCardDto() {
    }

    public ProductCardDto(UUID productId, String name, String slug, BigDecimal price, String imageUrl,
                          String brand, String category) {
        this.productId = productId;
        this.name = name;
        this.slug = slug;
        this.price = price;
        this.imageUrl = imageUrl;
        this.brand = brand;
        this.category = category;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
