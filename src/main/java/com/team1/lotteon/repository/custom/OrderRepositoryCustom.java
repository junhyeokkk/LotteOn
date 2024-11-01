package com.team1.lotteon.repository.custom;

import com.team1.lotteon.entity.Order;
import com.team1.lotteon.entity.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/*
    날짜 : 2024/10/31
    이름 : 최준혁
    내용 : OrderRepositoryCustom 생성
*/
@Repository
public interface OrderRepositoryCustom {
    Page<Order> findByDynamicType(String keyword, String type, Pageable pageable);
    Page<Order> findByDynamicTypeWithShopId(String keyword, String type, Pageable pageable, Long shopId);
    Page<Order> findByShopId(Long shopId, Pageable pageable);
}
