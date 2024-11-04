package com.team1.lotteon.repository;

import com.team1.lotteon.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
