package com.team1.lotteon.repository.shop;
/*
     날짜 : 2024/10/21
     이름 : 이도영
     내용 : 상점 관련 서비스

     수정이력
      - 2024/10/25 이도영 - 유효성검사
      - 2024/10/28 이도영 - 관리자 상점 페이지 출력
      - 2024/11/06 이도영 - 판매자 이메일 기능 추가
*/
import com.team1.lotteon.entity.Shop;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> , ShopRepositoryCustom{
    // 상호명으로 검색
    Page<Shop> findByShopNameContaining(String shopName, Pageable pageable);
    // 대표자명으로 검색
    Page<Shop> findByRepresentativeContaining(String representative, Pageable pageable);
    // 사업자 등록번호로 검색
    Page<Shop> findByBusinessRegistrationContaining(String businessRegistration, Pageable pageable);
    // 연락처로 검색
    Page<Shop> findByPhContaining(String ph, Pageable pageable);
    // 키워드로 검색 (모든 필드에 대해 검색)
    Page<Shop> findByShopNameContainingOrRepresentativeContainingOrBusinessRegistrationContainingOrPhContaining(
            String shopName, String representative, String businessRegistration, String ph, Pageable pageable);

    boolean existsByShopName(String shopname);
    boolean existsByBusinessRegistration(String businessRegistration);
    //판매자 존재여부 확인
    boolean existsByEmail(String email);
    //판매자 의 이름과 이메일 검색

    Shop findByRepresentativeAndEmail(String name, String email);
    //통신 판매자 번호 검색
    @Query("SELECT COUNT(s.id) > 0 FROM Shop s WHERE s.eCommerceRegistration = :eco")
    boolean existsByECommerceRegistration(@Param("eco") String eco);
    //전화 번호 존재 여부 검색
    boolean existsByph(String ph);
    //팩스 번호 존재 여부 검색
    boolean existsByFax(String fax);
}
