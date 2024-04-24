package com.example.demo.repository.shopping.cart;

import com.example.demo.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("FROM ShoppingCart sc WHERE sc.user.email = :email")
    Optional<ShoppingCart> findByUserEmail(@Param("email") String email);
}
