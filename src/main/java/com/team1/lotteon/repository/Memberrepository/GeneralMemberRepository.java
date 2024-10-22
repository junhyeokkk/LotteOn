package com.team1.lotteon.repository.Memberrepository;

import com.team1.lotteon.entity.GeneralMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralMemberRepository extends JpaRepository<GeneralMember, String>{
    boolean existsByEmail(String uid);
    boolean existsByph(String ph);
}
