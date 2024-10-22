package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Article, Integer> {
}
