package com.team1.lotteon.repository.productCustom;

import com.team1.lotteon.dto.product.ProductSearchRequestDto;
import com.team1.lotteon.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {
    Page<Product> searchProducts(ProductSearchRequestDto searchRequestDto, List<Long> categoryIds, Pageable pageable);
}
