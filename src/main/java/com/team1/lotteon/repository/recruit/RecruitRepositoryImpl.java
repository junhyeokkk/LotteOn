package com.team1.lotteon.repository.recruit;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.lotteon.entity.QRecruit;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
/*
    날짜 : 2024/11/06
    이름 : 강유정
    내용 : RecruitRepositoryImpl 생성
*/
@Log4j2
@RequiredArgsConstructor
@Repository
public class RecruitRepositoryImpl implements RecruitRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;
    private QRecruit qRecruit = QRecruit.recruit;
    @Override
    public Page<Tuple> findByDynamicType(String keyword, String type, Pageable pageable) {
        List<Tuple> content = null;
        long total = 0;
        content = queryFactory.select(qRecruit,qRecruit)
                .from(qRecruit)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qRecruit.recruitid.desc())
                .fetch();
        total = queryFactory
                .select(qRecruit.count())
                .from(qRecruit)
                .fetchOne();

        return new PageImpl<Tuple>(content, pageable, total);
    }

}