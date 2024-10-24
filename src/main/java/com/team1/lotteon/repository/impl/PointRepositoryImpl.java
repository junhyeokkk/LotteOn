package com.team1.lotteon.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.lotteon.entity.Point;
import com.team1.lotteon.entity.QPoint;
import com.team1.lotteon.repository.PointRepository;
import com.team1.lotteon.repository.custom.PointRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PointRepositoryImpl implements PointRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Point> findByDynamicType(String keyword, String type, Pageable pageable) {
        QPoint point = QPoint.point;

        // 기본 쿼리 작성
        var query = queryFactory.selectFrom(point)
                .where(getPredicate(keyword, type));

        // 페이징 처리
        List<Point> result = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query.fetchCount();

        return new PageImpl<>(result, pageable, total);
    }

    private BooleanExpression getPredicate(String keyword, String type) {
        if (keyword == null || keyword.isEmpty() || type == null || type.isEmpty()) {
            return null; // 조건이 없을 경우 null 반환
        }

        // 동적으로 필드 선택
        switch (type.toLowerCase()) {
            case "type":
                return QPoint.point.type.stringValue().containsIgnoreCase(keyword);
            case "acpoints":
                return QPoint.point.acPoints.stringValue().containsIgnoreCase(keyword);
            case "member_id":
                return QPoint.point.member.uid.containsIgnoreCase(keyword); // GeneralMember의 id 사용
            // 추가적인 필드를 필요에 따라 추가
            default:
                return null;
        }
    }
}
