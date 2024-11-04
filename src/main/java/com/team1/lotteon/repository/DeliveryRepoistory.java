package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Delivery;
import com.team1.lotteon.entity.OrderItem;
import com.team1.lotteon.entity.enums.DeliveryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/*
    날짜 : 2024/10/31
    이름 : 최준혁
    내용 : delivery 리파지토리 생성
*/
public interface DeliveryRepoistory extends JpaRepository<Delivery,Integer> {
    public Delivery findByorderItem(OrderItem orderItem);

    // ADMIN: 모든 상태의 Delivery를 페이징 조회
    Page<Delivery> findByStatusNot(DeliveryStatus status, Pageable pageable);

    // SELLER: 본인 가게의 Delivery만 페이징 조회
    Page<Delivery> findByStatusNotAndOrderItem_Product_Shop_Id(
            DeliveryStatus status, Long shopId, Pageable pageable);
}
