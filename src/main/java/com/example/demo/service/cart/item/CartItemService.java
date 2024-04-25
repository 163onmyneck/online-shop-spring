package com.example.demo.service.cart.item;

import com.example.demo.dto.cart.item.CartItemRequestDto;
import com.example.demo.dto.cart.item.CartItemResponseDto;
import com.example.demo.dto.shopping.cart.ShoppingCartDto;

public interface CartItemService {
    CartItemResponseDto saveItemToShoppingCart(CartItemRequestDto cartItemRequestDto,
                                               ShoppingCartDto shoppingCartDto);

    CartItemResponseDto getById(Long id);

    void deleteById(Long id);

    void addQuantity(Long id, int quantity);
}
