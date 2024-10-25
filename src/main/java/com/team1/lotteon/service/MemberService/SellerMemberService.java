package com.team1.lotteon.service.MemberService;

import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.SellerMember;
import com.team1.lotteon.repository.Memberrepository.SellerMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class SellerMemberService {
    private final SellerMemberRepository sellerMemberRepository;
    public void insertSellerMember(SellerMember sellerMember) {
        sellerMemberRepository.save(sellerMember);
    }
}
