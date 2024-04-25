package com.example.demo.repository.order;

import com.example.demo.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN o.user u WHERE u.email = :email")
    List<Order> findAllByUserEmail(@Param("email") String email);

    @Query("SELECT o FROM Order o JOIN o.user u WHERE u.email = :email")
    Optional<Order> findByUserEmail(@Param("email") String email);
}
