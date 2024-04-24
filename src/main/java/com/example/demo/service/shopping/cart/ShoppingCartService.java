package com.example.demo.service.shopping.cart;

import com.example.demo.dto.shopping.cart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getByUserEmail(String email);
}
