package com.team1.lotteon.repository.recruit;

import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitRepositoryCustom {
    Page<Tuple> findByDynamicType(String keyword, String type, Pageable pageable);
}
