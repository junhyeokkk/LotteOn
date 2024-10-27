package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.Productdetail;
import org.springframework.data.jpa.repository.JpaRepository;

/*
    날짜 : 2024/10/27
    이름 : 최준혁
    내용 : 상품 상세정보 리파지토리 생성
*/
public interface ProductDetailRepository extends JpaRepository<Productdetail, Long> {

}
