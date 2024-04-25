package com.example.demo.service.cart.item.impl;

import com.example.demo.dto.cart.item.CartItemRequestDto;
import com.example.demo.dto.cart.item.CartItemResponseDto;
import com.example.demo.dto.shopping.cart.ShoppingCartDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.model.CartItem;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.cart.item.CartItemRepository;
import com.example.demo.service.cart.item.CartItemService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;

    @Transactional
    @Override
    public CartItemResponseDto saveItemToShoppingCart(CartItemRequestDto cartItemRequestDto,
                                                      ShoppingCartDto shoppingCartDto) {
        CartItem item = cartItemMapper.toModel(cartItemRequestDto);
        item.setBook(bookRepository.findById(cartItemRequestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException("Can not find book by id:"
                + cartItemRequestDto.getBookId())));
        shoppingCartDto.getCartItems().add(
                        cartItemMapper.toDto(cartItemRepository
                .save(item)));
        return cartItemMapper.toDto(
            cartItemRepository.save(item));
    }

    @Transactional
    @Override
    public CartItemResponseDto getById(Long id) {
        return cartItemMapper.toDto(cartItemRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Can not find cart item by id:" + id)));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public void addQuantity(Long id, int quantity) {
        Optional<CartItem> cartItem = cartItemRepository.findById(id);
        if (cartItem.isPresent()) {
            cartItem.get().setQuantity(quantity);
            cartItemRepository.save(cartItem.get());
            return;
        }
        throw new EntityNotFoundException("Can not find cart item with id:" + id);
    }
}
