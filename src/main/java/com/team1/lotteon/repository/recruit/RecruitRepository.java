package com.team1.lotteon.repository.recruit;
/*
    날짜 : 2024/11/01
    이름 : 강유정
    내용 : 채용 리파지토리 생성
*/

import com.team1.lotteon.dto.RecruitDTO;
import com.team1.lotteon.entity.Recruit;
import com.team1.lotteon.repository.recruit.RecruitRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long>, RecruitRepositoryCustom {
    Page<Recruit> findByMemberUid(String uid, Pageable pageable);
    Page<Recruit> findByStatus(String status, Pageable pageable);

    @Transactional
    @Modifying
    @Query("DELETE FROM Recruit r WHERE r.recruitid IN :recruitIds")
    void deleteAllByIdIn(@Param("recruitIds") List<Long> recruitIds);

    Page<Recruit> findByRecruitid(String keyword,Pageable pageable);
    Page<Recruit> findByPositionContaining(String keyword,Pageable pageable);

    Page<Recruit> findByTypeContaining(String keyword,Pageable pageable);
    Page<Recruit> findByTitleContaining(String keyword,Pageable pageable);
}