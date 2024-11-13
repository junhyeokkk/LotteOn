package com.team1.lotteon.repository;

import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.Point;
import com.team1.lotteon.entity.enums.TransactionType;
import com.team1.lotteon.repository.custom.PointRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/*
    날짜 : 2024/10/23
    이름 : 최준혁
    내용 : 포인트 레파지토리 생성
*/
@Repository
public interface PointRepository extends JpaRepository<Point, Long>, PointRepositoryCustom {
    // 특정 member_id의 포인트를 가져오는 메서드
    Page<Point> findByMemberUid(String uid, Pageable pageable);

    List<Point> findByMemberOrderByCreatedatAsc(GeneralMember member);


    Page<Point> findByMember_UidAndCreatedatBetween(String uid, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    List<Point> findByExpirationDateBeforeAndTransactionType(LocalDateTime dateTime, TransactionType transactionType);


}
