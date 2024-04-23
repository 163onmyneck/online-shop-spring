package com.example.demo.service.shopping.cart.impl;

import com.example.demo.dto.shopping.cart.ShoppingCartDto;
import com.example.demo.mapper.ShoppingCartMapper;
import com.example.demo.repository.shopping.cart.ShoppingCartRepository;
import com.example.demo.service.shopping.cart.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartDto getByUserId(Long id) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUserId(id));
    }
}
