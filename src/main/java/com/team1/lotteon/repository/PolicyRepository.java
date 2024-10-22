package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<Term, Long> {

    Optional<Term> findByTermCode(String termCode);  // termCode가 카테고리를 저장하는 필드라 가정

}

