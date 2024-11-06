package com.team1.lotteon.repository.Memberrepository;

import com.team1.lotteon.entity.SellerMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerMemberRepository extends JpaRepository<SellerMember, String> {
    Optional<SellerMember> findByUid(String uid);
    List<SellerMember> findByShopShopNameContaining(String shopName);
    //2024/11/06 이도영 이메일과 아이디로 비밀번호 검색
    Optional<SellerMember> findByUidAndShop_Email(String uid, String email);
}
