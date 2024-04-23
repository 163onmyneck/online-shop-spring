package com.example.demo.service.cart.item.impl;

import com.example.demo.dto.cart.item.CartItemRequestDto;
import com.example.demo.dto.cart.item.CartItemResponseDto;
import com.example.demo.dto.shopping.cart.ShoppingCartDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.cart.item.CartItemRepository;
import com.example.demo.service.cart.item.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;

    @Override
    public CartItemResponseDto saveItemToShoppingCart(CartItemRequestDto cartItemRequestDto,
                                                      ShoppingCartDto shoppingCartDto) {
        shoppingCartDto.getCartItems().add(
                        cartItemMapper.toDto(cartItemRepository
                .save(cartItemMapper.toModel(cartItemRequestDto, bookRepository))));
        return cartItemMapper.toDto(
            cartItemRepository.save(cartItemMapper.toModel(cartItemRequestDto, bookRepository)));
    }

    @Override
    public CartItemResponseDto getById(Long id) {
        return cartItemMapper.toDto(cartItemRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Can not find cart item by id:" + id)));
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
