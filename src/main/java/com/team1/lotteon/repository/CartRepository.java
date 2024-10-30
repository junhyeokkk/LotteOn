package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/*
    날짜 : 2024/10/29
    이름 : 최준혁
    내용 : 카트 리파지토리 생성

    - 로그인한 유저 cart select 메서드 생성
*/

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    // member_id로 Cart 목록 조회
    public List<Cart> findByMemberUid(String memberId);
}
