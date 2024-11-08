package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Cart;
import com.team1.lotteon.entity.Member;
import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


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

    // 옵션이 있는 경우: member와 product, productOptionCombination을 기준으로 검색
    Optional<Cart> findByMemberAndProductAndProductOptionCombination(Member member, Product product, ProductOptionCombination optionCombination);

    // 옵션이 없는 경우: member와 product만 기준으로 검색 (productOptionCombination은 null)
    Optional<Cart> findByMemberAndProductAndProductOptionCombinationIsNull(Member member, Product product);

    // 조합삭제경우
    List<Cart> findByProductOptionCombination(ProductOptionCombination combination);
}
