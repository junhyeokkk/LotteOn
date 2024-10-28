package com.team1.lotteon.repository.shop;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.lotteon.dto.pageDTO.NewPageRequestDTO;
import com.team1.lotteon.entity.QShop;
import com.team1.lotteon.entity.Shop;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
@RequiredArgsConstructor
@Repository
public class ShopRepositoryImpl implements ShopRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;
    private QShop qshop = QShop.shop;

    @Override
    public Shop selectShop(int shop) {
        return null;
    }

    @Override
    public Page<Tuple> selectShopAllForList(NewPageRequestDTO newPageRequestDTO, Pageable pageable) {
        List<Tuple> content = null;
        long total = 0;
        content = queryFactory.select(qshop,qshop)
                .from(qshop)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qshop.id.desc())
                .fetch();
        total = queryFactory
                .select(qshop.count())
                .from(qshop)
                .fetchOne();
        return new PageImpl<Tuple>(content, pageable, total);
    }
}
