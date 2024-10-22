package com.team1.lotteon.service;

import com.team1.lotteon.entity.Term;
import com.team1.lotteon.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PolicyService {

    private final PolicyRepository policyRepository;

    public PolicyService(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    // 카테고리에 따라 약관을 가져오는 메서드
    public Term getTermsByCategory(String termCode) {
        // 카테고리에 해당하는 termCode를 기반으로 약관을 조회
        Optional<Term> termOptional = policyRepository.findByTermCode(termCode); // termCode를 기준으로 조회
        return termOptional.orElse(null); // 존재하지 않으면 null 반환
    }

    public void updateTerm(Term term) {
        if (term != null) {
            policyRepository.save(term); // JPA를 사용하여 DB에 저장
        } else {
            // 예외 처리 또는 로깅 추가
            throw new IllegalArgumentException("Term cannot be null");
        }
    }
}