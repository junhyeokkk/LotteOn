package com.team1.lotteon.service;

import com.team1.lotteon.dto.ResultDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 카테고리 서비스 생성

    - 수정내역
    - 자식 카테고리 가져오기 <캐싱 추가> getSubCategoriesByParentId() 추가 (10/25)
    - 2024/11/08 박서홍 - redis 캐싱작업
*/
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    // 자식 카테고리 가져오기 <캐싱 추가> (준혁)
    @Cacheable(value = "subCategories", key = "#parentId")
    public List<CategoryWithChildrenResponseDTO> getSubCategoriesByParentId(Long parentId) {
        List<Category> subCategories = categoryRepository.findByParentIdOrderByDisplayOrderAsc(parentId);
        return subCategories.stream()
                .map(category -> modelMapper.map(category, CategoryWithChildrenResponseDTO.class))
                .collect(Collectors.toList());
    }

    // 모든 1차 카테고리 조회 (상훈)

    @Cacheable(value = "allCategories", key = "'getAllRootCategories'")
    public ResultDTO<List<CategoryWithChildrenResponseDTO>> getAllRootCategories() {
        List<Category> allCate = categoryRepository.findAllRoot();
        return  new ResultDTO<>(allCate.stream().map(CategoryWithChildrenResponseDTO::fromEntity).toList());
    }

    public CategoryWithParentAndChildrenResponseDTO getDetailById(Long id) {
        Category category = categoryRepository.findWithChildrenAndParentById(id).orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
        return CategoryWithParentAndChildrenResponseDTO.fromEntity(category);
    }

    public List<CategoryResponseDTO> getParentList(Long id) {
        Category category = categoryRepository.findWithParentById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다."));

        List<CategoryResponseDTO> parentList = new ArrayList<>();
        buildParentList(category, parentList);

        return parentList;
    }

    private void buildParentList(Category category, List<CategoryResponseDTO> parentList) {
        if (category.getParent() != null) {
            // 상위 부모부터 먼저 추가되도록 부모 카테고리를 먼저 리스트에 추가
            buildParentList(category.getParent(), parentList);
        }
        // 현재 카테고리를 DTO로 변환하여 리스트에 추가
        parentList.add(new CategoryResponseDTO(category.getId(), category.getName(), (long) category.getLevel()));
    }

    public List<CategoryResponseDTO> getAllCategoriesByParentId(Long parentId) {
        List<Category> categories = categoryRepository.findByParentIdOrderByDisplayOrderAsc(parentId);
        return categories.stream().map(CategoryResponseDTO::fromEntity).toList();
    }

    public List<CategoryResponseDTO> getChildrenById(Long id) {
        Category parentcategory = categoryRepository.findWithChildrenById(id).orElseThrow(() -> new IllegalArgumentException("해당 카테고리는 존재하지 않습니다."));
        List<Category> children = parentcategory.getChildren();
        return children.stream().map(CategoryResponseDTO::fromEntity).toList();
    }

    public CategoryResponseDTO createCategory(CategoryCreateDTO categoryCreateDTO) {


        Category category = categoryCreateDTO.toEntity();
        Integer maxDisplayOrder = null;
        if (categoryCreateDTO.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(Long.valueOf(categoryCreateDTO.getParentId())).orElse(null);
            category.changeParent(parentCategory);

            maxDisplayOrder = categoryRepository.findMaxDisplayOrderByParentId(categoryCreateDTO.getParentId());
        } else {
            maxDisplayOrder = categoryRepository.findMaxDisplayOrderByRoot();
        }

        int newDisplayOrder = (maxDisplayOrder != null ? maxDisplayOrder : 0) + 10;

        category.changeDisplayOrder(newDisplayOrder);
        categoryRepository.save(category);

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

    public void updateCategoryDisplayOrder(Long id, Long targetId) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다."));
        Category targetCategory = categoryRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다."));

        if (category.getParent() == targetCategory.getParent() && category.getLevel() == targetCategory.getLevel()) {
            category.changeDisplayOrderWithTargetCate(targetCategory);
        }else {
            throw new IllegalArgumentException("카테고리의 레벨 또는 부모가 다릅니다");
        }

    }
}
