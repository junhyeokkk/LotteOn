package com.team1.lotteon.repository.Memberrepository;

import com.team1.lotteon.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    boolean existsByUid(String uid);
}
