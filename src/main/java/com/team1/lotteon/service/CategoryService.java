package com.team1.lotteon.service;

import com.team1.lotteon.dto.category.CategoryCreateDTO;
import com.team1.lotteon.dto.category.CategoryResponseDTO;
import com.team1.lotteon.dto.category.CategoryWithChildrenResponseDTO;
import com.team1.lotteon.entity.Category;
import com.team1.lotteon.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryWithChildrenResponseDTO> getAllCategories() {
        List<Category> allCate = categoryRepository.findAllWithCategory();
        log.debug("all cate size: {}", allCate.size());
        return allCate.stream().map(CategoryWithChildrenResponseDTO::fromEntity).toList();
    }

    public Category createCategory(CategoryCreateDTO categoryCreateDTO) {
        Category category = categoryCreateDTO.toEntity();
        categoryRepository.save(category);

        if (categoryCreateDTO.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(Long.valueOf(categoryCreateDTO.getParentId())).orElse(null);
            category.setParent(parentCategory);
        }

        return category;
    }

    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다."));
        return CategoryResponseDTO.fromEntity(category);
    }

    public void updateCategoryName(Long productId, String newName) {
        Category category = categoryRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다."));
        category.changeName(newName);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
