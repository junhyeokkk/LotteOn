package com.team1.lotteon.service;

import com.team1.lotteon.repository.CategoryRepository;
import com.team1.lotteon.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    
}
