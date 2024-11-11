package com.team1.lotteon.repository;

import com.team1.lotteon.entity.OrderItem;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByProductOptionCombination(ProductOptionCombination combination);
    // 특정 Order에 속한 모든 OrderItem을 찾기 위한 메서드
    List<OrderItem> findByOrder_Id(Long orderId);
}
