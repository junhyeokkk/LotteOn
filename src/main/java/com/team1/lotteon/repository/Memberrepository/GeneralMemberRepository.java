package com.team1.lotteon.repository.Memberrepository;

import com.team1.lotteon.entity.GeneralMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeneralMemberRepository extends JpaRepository<GeneralMember, String>{
    boolean existsByEmail(String uid);
    boolean existsByph(String ph);
    Optional<GeneralMember> findByUid(String uid);

    Page<GeneralMember> findByUidContaining(String uid, Pageable pageable);
    Page<GeneralMember> findByNameContaining(String name, Pageable pageable);
    Page<GeneralMember> findByEmailContaining(String email, Pageable pageable);
    Page<GeneralMember> findByPhContaining(String ph, Pageable pageable);
}
