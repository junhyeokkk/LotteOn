package com.team1.lotteon.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.lotteon.entity.Point;
import com.team1.lotteon.entity.QPoint;
import com.team1.lotteon.repository.PointRepository;
import com.team1.lotteon.repository.custom.PointRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/*
    날짜 : 2024/10/25
    이름 : 최준혁
    내용 : PointRepositoryImpl 생성

    - QueryDSL 사용하여 검색기능 메서드 추가
*/
@Log4j2
@RequiredArgsConstructor
@Repository
public class PointRepositoryImpl implements PointRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Point> findByDynamicType(String keyword, String type, Pageable pageable) {
        QPoint point = QPoint.point;

        // 검색 조건에 따른 where 조건 표현식 생성
        BooleanExpression expression = getPredicate(keyword, type);

        // keyword가 없을 때 빈 결과 반환
        if (expression == null) {
            return new PageImpl<>(List.of(), pageable, 0); // 빈 리스트와 총 0을 반환
        }

        // 기본 쿼리 작성
        List<Point> result = queryFactory.selectFrom(point)
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(point.createdat.desc()) // 정렬 기준 설정
                .fetch();

        long total = queryFactory.selectFrom(point)
                .where(expression)
                .fetchCount();

        return new PageImpl<>(result, pageable, total);
    }

    private BooleanExpression getPredicate(String keyword, String type) {
        if (keyword == null || keyword.isEmpty() || type == null || type.isEmpty()) {
            return null; // 조건이 없을 경우 null 반환
        }

        // 검색 선택 조건에 따라 where 조건 표현식 생성
        BooleanExpression expression = null;

        if (type.equals("type")) {
            expression = QPoint.point.type.containsIgnoreCase(keyword);
            log.info("Type expression: " + expression);
        } else if (type.equals("name")) {
            expression = QPoint.point.member.name.containsIgnoreCase(keyword);
        } else if (type.equals("member_id")) {
            expression = QPoint.point.member.uid.containsIgnoreCase(keyword); // GeneralMember의 id 사용
        }

        return expression; // 생성된 조건 반환
    }
}
