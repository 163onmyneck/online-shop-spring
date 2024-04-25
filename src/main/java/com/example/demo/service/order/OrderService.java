package com.example.demo.service.order;

import com.example.demo.dto.item.order.OrderItemDto;
import com.example.demo.dto.order.OrderDto;
import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrders(String email);

    OrderDto setOrderStatus(Long id, String status);

    OrderDto saveOrder(String email, String shippingAddress);

    List<OrderItemDto> getItems(Long id);

    OrderItemDto getById(Long orderId, Long itemId);
}
