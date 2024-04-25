package com.example.demo.controller;

import com.example.demo.dto.cart.item.CartItemRequestDto;
import com.example.demo.dto.cart.item.CartItemResponseDto;
import com.example.demo.dto.shopping.cart.ShoppingCartDto;
import com.example.demo.service.cart.item.CartItemService;
import com.example.demo.service.shopping.cart.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return shoppingCartService.getByUserEmail(userDetails.getUsername());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public CartItemResponseDto addBook(@RequestBody CartItemRequestDto cartItemRequestDto,
                                       Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return cartItemService.saveItemToShoppingCart(cartItemRequestDto,
            shoppingCartService.getByUserEmail(userDetails.getUsername()));
    }

    @PutMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void updateQuantity(@PathVariable Long id, int quantity) {
        cartItemService.addQuantity(id, quantity);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteById(@PathVariable Long id) {
        cartItemService.deleteById(id);
    }
}
