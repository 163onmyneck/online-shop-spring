package com.example.demo.controller;

import com.example.demo.dto.item.order.OrderItemDto;
import com.example.demo.dto.order.OrderDto;
import com.example.demo.service.order.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class OrderController {
    private OrderService orderService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public OrderDto updateStatus(@PathVariable Long id, @RequestBody String status) {
        return orderService.setOrderStatus(id, status);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public OrderDto placeAnOrder(@RequestBody String shippingAddress,
                                                Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return orderService.saveOrder(userDetails.getUsername(), shippingAddress);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/get-all")
    public List<OrderDto> getAllOrders(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return orderService.getAllOrders(userDetails.getUsername());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/{orderId}/items")
    public List<OrderItemDto> getItemsFromOrder(@PathVariable Long orderId) {
        return orderService.getItems(orderId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/{orderId}/items/{itemId}")
    public OrderItemDto getOneItemFromOrder(@PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService.getById(orderId, itemId);
    }
}
