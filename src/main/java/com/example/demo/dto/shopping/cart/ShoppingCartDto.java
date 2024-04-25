package com.example.demo.dto.shopping.cart;

import com.example.demo.dto.cart.item.CartItemResponseDto;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
