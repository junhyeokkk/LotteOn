package com.team1.lotteon.repository.productCustom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.lotteon.dto.product.ProductSearchRequestDto;
import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom  {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> searchProducts(ProductSearchRequestDto searchRequestDto, Pageable pageable) {
        QProduct product = QProduct.product;

        // BooleanBuilder로 동적 조건 설정
        BooleanBuilder builder = new BooleanBuilder();

        if (searchRequestDto.getCategoryId() != null) {
            builder.and(product.category.id.eq(searchRequestDto.getCategoryId()));
        }

        if (searchRequestDto.getKeyword() != null && !searchRequestDto.getKeyword().isEmpty()) {
            builder.and(product.productName.containsIgnoreCase(searchRequestDto.getKeyword()));
        }

        // QueryDSL로 동적 쿼리 실행
        List<Product> products = queryFactory
                .selectFrom(product)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(pageable.getSort().stream()
                        .map(order -> order.isAscending() ? product.get(order.getProperty()).asc() : product.get(order.getProperty()).desc())
                        .toArray(com.querydsl.core.types.OrderSpecifier[]::new))
                .fetch();

        // 전체 카운트 쿼리
        long total = queryFactory
                .selectFrom(product)
                .where(builder)
                .fetchCount();

        return PageableExecutionUtils.getPage(products, pageable, () -> total);
    }
}
