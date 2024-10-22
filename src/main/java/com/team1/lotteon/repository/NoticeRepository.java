package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Article;
import com.team1.lotteon.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
}
