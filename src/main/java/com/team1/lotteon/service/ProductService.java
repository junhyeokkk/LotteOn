package com.team1.lotteon.service;

import com.team1.lotteon.dto.category.CategoryCreateDTO;
import com.team1.lotteon.dto.category.CategoryResponseDTO;
import com.team1.lotteon.dto.category.CategoryWithChildrenResponseDTO;
import com.team1.lotteon.entity.Category;
import com.team1.lotteon.repository.CategoryRepository;
import com.team1.lotteon.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

}
