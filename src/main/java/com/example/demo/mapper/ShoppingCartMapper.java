package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.shopping.cart.ShoppingCartDto;
import com.example.demo.model.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    ShoppingCart toModel(ShoppingCartDto shoppingCartDto);
}
