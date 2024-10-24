package com.team1.lotteon.service;

import com.team1.lotteon.dto.category.CategoryCreateDTO;
import com.team1.lotteon.dto.category.CategoryResponseDTO;
import com.team1.lotteon.dto.category.CategoryWithChildrenResponseDTO;
import com.team1.lotteon.dto.category.CategoryWithParentAndChildrenResponseDTO;
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

    public List<CategoryWithChildrenResponseDTO> getAllRootCategories() {
        List<Category> allCate = categoryRepository.findAllRootWithChildren();
        return allCate.stream().map(CategoryWithChildrenResponseDTO::fromEntity).toList();
    }

    public CategoryWithParentAndChildrenResponseDTO getDetailById(Long id) {
        Category category = categoryRepository.findWithChildrenAndParentById(id).orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
        return CategoryWithParentAndChildrenResponseDTO.fromEntity(category);
    }

    public List<CategoryResponseDTO> getChildrenById(Long id) {
        Category parentcategory = categoryRepository.findWithChildrenById(id).orElseThrow(() -> new IllegalArgumentException("해당 카테고리는 존재하지 않습니다."));
        List<Category> children = parentcategory.getChildren();
        return children.stream().map(CategoryResponseDTO::fromEntity).toList();
    }

    public Category createCategory(CategoryCreateDTO categoryCreateDTO) {
        Category category = categoryCreateDTO.toEntity();
        categoryRepository.save(category);

        if (categoryCreateDTO.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(Long.valueOf(categoryCreateDTO.getParentId())).orElse(null);
            category.changeParent(parentCategory);
        }

        return category;
    }

    public void updateCategoryName(Long productId, String newName) {
        Category category = categoryRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다."));
        category.changeName(newName);
    }

    public void updateCategoryParent(Long productId, Long parentId) {
        Category category = categoryRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다."));
        Category parentCategory = categoryRepository.findById(parentId).orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다."));

        category.changeParent(parentCategory);
    }


    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다."));

        categoryRepository.deleteById(id);
    }
}
