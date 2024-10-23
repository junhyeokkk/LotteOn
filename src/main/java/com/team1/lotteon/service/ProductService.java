package com.team1.lotteon.service;

import com.team1.lotteon.dto.category.CategoryCreateDTO;
import com.team1.lotteon.dto.category.CategoryResponseDTO;
import com.team1.lotteon.dto.category.CategoryWithChildrenResponseDTO;
import com.team1.lotteon.dto.product.CreateProductDTO;
import com.team1.lotteon.entity.Category;
import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.enums.CateLevel;
import com.team1.lotteon.repository.CategoryRepository;
import com.team1.lotteon.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public void createProduct(CreateProductDTO createProductDTO) {
        Category category = categoryRepository.findByIdAndLevel(createProductDTO.getCategoryId(), CateLevel.THREE).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        Product product = createProductDTO.toEntity();
        productRepository.save(product);
        product.changeCategory(category);
    }
}
