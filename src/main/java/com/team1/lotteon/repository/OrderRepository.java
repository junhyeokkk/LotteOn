package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Order;
import com.team1.lotteon.entity.Point;
import com.team1.lotteon.entity.productOption.ProductOption;
import com.team1.lotteon.repository.custom.OrderRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/*
    날짜 : 2024/10/31
    이름 : 최준혁
    내용 : 오더 리파지토리 생성
    수정내용
        - 2024/11/06 이도영 나의정보 전체화면에 주문량 출력
*/
public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    // SellerMember가 모든 주문을 조회할 때 shopId로 필터링
//    Page<Order> findByShopId(Long shopId, Pageable pageable);


    // 가장 최근 주문 1개를 반환
    public List<Order> findTop5ByMember_UidOrderByOrderDateDesc(String uid);

    //2024/11/06 이도영 나의정보 전체화면에 주문량 출력
    List<Order> findAllByMember_Uid(String userId);

    Page<Order> findByMember_UidAndOrderDateBetween(String uid, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);


    Page<Order> findByMemberUidAndOrderDateBetween(String uid, LocalDateTime start, LocalDateTime end, Pageable pageable);
}