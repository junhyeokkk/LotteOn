package com.team1.lotteon.repository.productCustom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.lotteon.dto.product.ProductSearchRequestDto;
import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.QProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> searchProducts(ProductSearchRequestDto searchRequestDto, List<Long> categoryIds , Pageable pageable) {
        QProduct product = QProduct.product;

        BooleanBuilder builder = new BooleanBuilder();

        if (searchRequestDto.getKeyword() != null && !searchRequestDto.getKeyword().isEmpty()) {
            builder.and(product.productName.containsIgnoreCase(searchRequestDto.getKeyword()));
        }

        if(categoryIds != null && !categoryIds.isEmpty()) {
            builder.and(product.category.id.in(categoryIds));
        }

        // 동적 정렬 설정
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(pageable.getSort(), product);

        List<Product> products = queryFactory
                .selectFrom(product)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .fetch();


        long total = queryFactory
                .selectFrom(product)
                .where(builder)
                .fetchCount();

        return PageableExecutionUtils.getPage(products, pageable, () -> total);
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort, QProduct product) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        for (Sort.Order order : sort) {
            OrderSpecifier<?> orderSpecifier = switch (order.getProperty()) {
                case "createdAt" -> order.isAscending() ? product.createdAt.asc() : product.createdAt.desc();
                case "price" -> order.isAscending() ? product.price.asc() : product.price.desc();
                case "name" -> order.isAscending() ? product.productName.asc() : product.productName.desc();
                default -> throw new IllegalArgumentException("정렬할 수 없는 필드입니다: " + order.getProperty());
            };
            orderSpecifiers.add(orderSpecifier);
        }
        return orderSpecifiers;
    }
}
