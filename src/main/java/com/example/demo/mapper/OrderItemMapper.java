package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.item.order.OrderItemDto;
import com.example.demo.model.OrderItem;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    OrderItemDto toDto(OrderItem orderItem);

    List<OrderItemDto> toDtos(Set<OrderItem> orderItems);
}
