package com.example.electro_shop.modules.carts.mappers;

import com.example.electro_shop.modules.carts.dtos.CartItemResponse;
import com.example.electro_shop.modules.carts.dtos.CartResponse;
import com.example.electro_shop.modules.carts.models.Cart;
import com.example.electro_shop.modules.carts.models.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(target = "totalAmount", source = "items", qualifiedByName = "calculateTotal")
    CartResponse toResponseDto(Cart cart);

    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "unitPrice", source = "product.price")
    @Mapping(target = "imageUrl", source = "product.imageUrl")
    @Mapping(target = "subtotal", source = "item", qualifiedByName = "calculateSubtotal")
    CartItemResponse toItemResponseDto(CartItem item);

    @Named("calculateSubtotal")
    default BigDecimal calculateSubtotal(CartItem item) {
        if (item == null || item.getProduct() == null) return BigDecimal.ZERO;
        return item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity()));
    }

    @Named("calculateTotal")
    default BigDecimal calculateTotal(List<CartItem> items) {
        if (items == null) return BigDecimal.ZERO;
        return items.stream()
                .map(this::calculateSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
