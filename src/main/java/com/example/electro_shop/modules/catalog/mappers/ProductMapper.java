package com.example.electro_shop.modules.catalog.mappers;

import com.example.electro_shop.modules.catalog.dtos.ProductCardDto;
import com.example.electro_shop.modules.catalog.dtos.ProductDetailDto;
import com.example.electro_shop.modules.catalog.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "brand.name", target = "brand")
    @Mapping(source = "category.name", target = "category")
    ProductCardDto toProductCardDto(Product product);

    @Mapping(source = "brand.name", target = "brand")
    @Mapping(source = "category.name", target = "category")
    ProductDetailDto toProductDetailDto(Product product);
}
