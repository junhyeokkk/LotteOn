package com.team1.lotteon.repository.Memberrepository;

import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.SellerMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerMemberRepository extends JpaRepository<SellerMember, String> {
    Optional<SellerMember> findByUid(String uid);

}
