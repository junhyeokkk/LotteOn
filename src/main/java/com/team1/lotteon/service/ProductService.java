package com.team1.lotteon.service;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.product.ProductCreateDTO;
import com.team1.lotteon.dto.product.ProductResponseDTO;
import com.team1.lotteon.dto.product.ProductSummaryResponseDTO;
import com.team1.lotteon.entity.Category;
import com.team1.lotteon.entity.Product;
import com.team1.lotteon.repository.CategoryRepository;
import com.team1.lotteon.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductResponseDTO createProduct(ProductCreateDTO productCreateDTO) {
        Category category = categoryRepository.findByIdAndLevel(productCreateDTO.getCategoryId(), 3).orElse(null);
        Product product = productCreateDTO.toEntity();
        product.changeCategory(category);
        productRepository.save(product);

        return ProductResponseDTO.fromEntity(product);
    }

    public PageResponseDTO<ProductSummaryResponseDTO> getProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return PageResponseDTO.fromPage(products.map(ProductSummaryResponseDTO::fromEntity));
    }
}
