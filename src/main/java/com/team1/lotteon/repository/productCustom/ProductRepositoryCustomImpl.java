package com.team1.lotteon.repository.productCustom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team1.lotteon.dto.product.ProductSearchRequestDto;
import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.QOrderItem;
import com.team1.lotteon.entity.QProduct;
import com.team1.lotteon.entity.QReview;
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
    public Page<Product> searchProducts(ProductSearchRequestDto searchRequestDto, List<Long> categoryIds, Pageable pageable) {
        QProduct product = QProduct.product;
        QOrderItem orderItem = QOrderItem.orderItem;
        QReview review = QReview.review;

        BooleanBuilder builder = new BooleanBuilder();

        if (searchRequestDto.getKeyword() != null && !searchRequestDto.getKeyword().isEmpty()) {
            Predicate predicate;
            if(searchRequestDto.getType() != null) {
                predicate = switch (searchRequestDto.getType()) {
                    case "prodName" -> product.productName.containsIgnoreCase(searchRequestDto.getKeyword());
                    case "prodNo" -> product.id.eq(Long.valueOf(searchRequestDto.getKeyword()));
                    case "sellerNo" -> product.member.shop.shopName.containsIgnoreCase(searchRequestDto.getKeyword());
                    case "prodCompany" -> product.manufacturer.containsIgnoreCase(searchRequestDto.getKeyword());
                    default -> product.productName.containsIgnoreCase(searchRequestDto.getKeyword());
                };
            }else {
                predicate = product.productName.containsIgnoreCase(searchRequestDto.getKeyword());
            }
            builder.and(predicate);
        }

        if (categoryIds != null && !categoryIds.isEmpty()) {
            builder.and(product.category.id.in(categoryIds));
        }
        // 동적 정렬 설정
        List<OrderSpecifier<?>> orderSpecifiers = getOrderSpecifiers(searchRequestDto.getSortBy(), product, orderItem, review);
        List<Product> products = queryFactory
                .selectFrom(product)
                .leftJoin(orderItem).on(orderItem.product.id.eq(product.id))
                .leftJoin(review).on(review.product.id.eq(product.id))
                .where(builder)
                .groupBy(product.id)
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

    private List<OrderSpecifier<?>> getOrderSpecifiers(String sortBy, QProduct product, QOrderItem orderItem, QReview review) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        OrderSpecifier<?> orderSpecifier;
        switch (sortBy) {
            case "prodSold": {
                orderSpecifier = orderItem.count().desc();
                break;
            }
            case "prodLowPrice": {
                orderSpecifier = Expressions.numberTemplate(Double.class, "({0} * (1 - {1} / 100.0))", product.price, product.discountRate).asc();
                break;
            }
            case "prodHighPrice": {
                orderSpecifier = Expressions.numberTemplate(Double.class, "({0} * (1 - {1} / 100.0))", product.price, product.discountRate).desc();
                break;
            }
            case "prodReview": {
                orderSpecifier = review.count().desc();
                break;
            }
            case "prodScore": {
                orderSpecifier = review.score.avg().desc();
                break;
            }
            case "prodRdate":
            default: {
                orderSpecifier = product.createdAt.desc();
            }
        }
        orderSpecifiers.add(orderSpecifier);
        orderSpecifiers.add(product.createdAt.desc());
        return orderSpecifiers;
    }
}
