package com.example.demo.controller;

import com.example.demo.dto.cart.item.CartItemRequestDto;
import com.example.demo.dto.cart.item.CartItemResponseDto;
import com.example.demo.dto.shopping.cart.ShoppingCartDto;
import com.example.demo.service.cart.item.CartItemService;
import com.example.demo.service.shopping.cart.ShoppingCartService;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
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
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;

    @GetMapping
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long currentUser = userService.getUserIdByEmail(userDetails.getUsername());
        return shoppingCartService.getByUserId(currentUser);
    }

    @PostMapping
    public CartItemResponseDto addBook(@RequestBody CartItemRequestDto cartItemRequestDto,
                                       Authentication authentication) {
        return cartItemService.saveItemToShoppingCart(cartItemRequestDto,
            getShoppingCart(authentication));
    }

    @PutMapping("/cart-items/{cartItemId}")
    public void updateQuantity(@PathVariable Long id, int quantity) {
        cartItemService.getById(id).setQuantity(quantity);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    public void deleteById(@PathVariable Long id) {
        cartItemService.deleteById(id);
    }
}
