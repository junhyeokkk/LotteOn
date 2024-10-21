package com.team1.lotteon.repository.Memberrepository;

import com.team1.lotteon.entity.SellerMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerMemberRepository extends JpaRepository<SellerMember, String> {

}
