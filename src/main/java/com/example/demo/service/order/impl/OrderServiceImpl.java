package com.example.demo.service.order.impl;

import com.example.demo.dto.item.order.OrderItemDto;
import com.example.demo.dto.order.OrderDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.OrderItemMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.Order.Status;
import com.example.demo.model.OrderItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.repository.item.order.OrderItemRepository;
import com.example.demo.repository.order.OrderRepository;
import com.example.demo.repository.shopping.cart.ShoppingCartRepository;
import com.example.demo.service.order.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return orderMapper.toDtos(orderRepository.findAllByUserEmail(email));
    }

    @Transactional
    @Override
    public OrderDto setOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Can not find order by id:" + id));

        order.setStatus(Status.valueOf(status));
        return orderMapper.toDto(orderRepository.save(order));

    }

    @Transactional
    @Override
    public OrderDto saveOrder(String email, String shippingAddress) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserEmail(email);
        if (shoppingCart.isEmpty()) {
            throw new EntityNotFoundException("Can not find shopping cart");
        }
        ShoppingCart userShoppingCart = shoppingCart.get();

        Order userOrder = new Order(userShoppingCart);
        userOrder.setShippingAddress(shippingAddress);
        userOrder.setTotal(userShoppingCart.getCartItems().stream()
                                                        .map(i -> i.getBook().getPrice())
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add));
        Set<OrderItem> orderItems = new HashSet<>();

        for (CartItem item : userShoppingCart.getCartItems()) {
            OrderItem orderItem = new OrderItem(item);
            orderItems.add(orderItem);
        }
        userOrder.setOrderItems(orderItems);
        return orderMapper.toDto(orderRepository.save(userOrder));
    }

    @Override
    public List<OrderItemDto> getItems(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Can not find order by id:" + id));

        return orderItemMapper.toDtos(order.getOrderItems());
    }

    @Override
    public OrderItemDto getById(Long orderId, Long itemId) {
        return orderItemMapper.toDto(orderItemRepository.getById(orderId, itemId).orElseThrow(
            () -> new EntityNotFoundException(
            "Can not find order by id:" + itemId)));
    }
}
