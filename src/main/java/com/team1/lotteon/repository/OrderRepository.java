package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Order;
import com.team1.lotteon.entity.productOption.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

/*
    날짜 : 2024/10/31
    이름 : 최준혁
    내용 : 오더 리파지토리 생성
*/
public interface OrderRepository extends JpaRepository<Order, Long> {
}
