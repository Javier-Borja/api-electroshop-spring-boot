package com.example.electro_shop.modules.orders.mappers;

import com.example.electro_shop.modules.orders.dtos.OrderResponse;
import com.example.electro_shop.modules.orders.dtos.OrderSummary;
import com.example.electro_shop.modules.orders.models.Order;
import com.example.electro_shop.modules.orders.models.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "itemCount", source = "order")
    OrderSummary toOrderSummary(Order order);

    default int mapItemCount(Order order) {
        if (order.getItems() == null) return 0;
        return order.getItems().stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }
}
