package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.cart.item.CartItemRequestDto;
import com.example.demo.dto.cart.item.CartItemResponseDto;
import com.example.demo.model.CartItem;
import com.example.demo.repository.book.BookRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    CartItemResponseDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toModel(CartItemRequestDto cartItemRequestDto, @Context BookRepository bookRepository);
}
