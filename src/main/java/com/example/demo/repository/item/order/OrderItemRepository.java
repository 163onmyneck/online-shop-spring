package com.example.demo.repository.item.order;

import com.example.demo.model.OrderItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("FROM OrderItem oi JOIN oi.order o WHERE o.id = :orderID AND oi.id = :itemId")
    Optional<OrderItem> getById(@Param("orderID") Long orderId, @Param("itemId") Long itemId);
}
