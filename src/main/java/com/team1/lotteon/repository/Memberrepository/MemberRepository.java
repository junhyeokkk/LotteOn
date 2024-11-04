package com.team1.lotteon.repository.Memberrepository;

import com.team1.lotteon.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    boolean existsByUid(String uid);
    // String 타입의 memberId로 Member 조회
    Optional<Member> findByUid(String uid);
}
