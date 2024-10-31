package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

/*
    날짜 : 2024/10/31
    이름 : 최준혁
    내용 : delivery 리파지토리 생성
*/
public interface DeliveryRepoistory extends JpaRepository<Delivery,Integer> {
}
