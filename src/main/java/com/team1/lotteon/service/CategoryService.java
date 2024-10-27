package com.team1.lotteon.service;

import com.team1.lotteon.dto.category.CategoryCreateDTO;
import com.team1.lotteon.dto.category.CategoryResponseDTO;
import com.team1.lotteon.dto.category.CategoryWithChildrenResponseDTO;
import com.team1.lotteon.dto.category.CategoryWithParentAndChildrenResponseDTO;
import com.team1.lotteon.entity.Category;
import com.team1.lotteon.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 카테고리 서비스 생성

    - 수정내역
    - 자식 카테고리 가져오기 <캐싱 추가> getSubCategoriesByParentId() 추가 (10/25)
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    // 자식 카테고리 가져오기 <캐싱 추가> (준혁)
    @Cacheable(value = "subCategories", key = "#parentId")
    public List<CategoryWithChildrenResponseDTO> getSubCategoriesByParentId(Long parentId) {
        List<Category> subCategories = categoryRepository.findByParentId(parentId);
        return subCategories.stream()
                .map(category -> modelMapper.map(category, CategoryWithChildrenResponseDTO.class))
                .collect(Collectors.toList());
    }

    // 모든 1차 카테고리 조회 (상훈)
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

    public CategoryResponseDTO createCategory(CategoryCreateDTO categoryCreateDTO) {
        Category category = categoryCreateDTO.toEntity();
        categoryRepository.save(category);

        if (categoryCreateDTO.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(Long.valueOf(categoryCreateDTO.getParentId())).orElse(null);
            category.changeParent(parentCategory);
        }

        return CategoryResponseDTO.fromEntity(category);
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
