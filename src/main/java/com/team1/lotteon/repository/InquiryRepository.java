package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Article;
import com.team1.lotteon.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Integer> {
}
