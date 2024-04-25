package com.example.demo.service.order.impl;

import com.example.demo.dto.item.order.OrderItemDto;
import com.example.demo.dto.order.OrderDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.OrderItemMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.Order;
import com.example.demo.model.Order.Status;
import com.example.demo.model.ShoppingCart;
import com.example.demo.repository.item.order.OrderItemRepository;
import com.example.demo.repository.order.OrderRepository;
import com.example.demo.repository.shopping.cart.ShoppingCartRepository;
import com.example.demo.service.order.OrderService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;

    @Override
    public List<OrderDto> getAllOrders(String email) {
        return orderRepository.findAllByUserEmail(email).stream()
                                                        .map(orderMapper::toDto)
                                                        .toList();
    }

    @Override
    public OrderDto setOrderStatus(Long id, String status) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            order.get().setStatus(Status.valueOf(status));
            return orderMapper.toDto(orderRepository.save(order.get()));
        }
        throw new EntityNotFoundException("Can not find order with email: " + id);
    }

    @Override
    public OrderDto saveOrder(String email, String shippingAddress) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserEmail(email);
        if (shoppingCart.isEmpty()) {
            throw new EntityNotFoundException("Can not find shopping cart");
        }
        Order model = orderMapper.toModel(shoppingCart.get());

        model.setOrderItems(shoppingCart.get().getCartItems().stream()
                                                            .map(orderItemMapper::toModel)
                                                            .collect(Collectors.toSet()));

        return orderMapper.toDto(orderRepository.save(model));
    }

    @Override
    public List<OrderItemDto> getItems(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
            "Can not find order by id:" + id)).getOrderItems().stream()
            .map(orderItemMapper::toDto)
            .toList();
    }

    @Override
    public OrderItemDto getById(Long orderId, Long itemId) {
        return orderItemMapper.toDto(orderItemRepository.getById(orderId, itemId).orElseThrow(
            () -> new EntityNotFoundException(
            "Can not find order by id:" + itemId)));
    }
}
