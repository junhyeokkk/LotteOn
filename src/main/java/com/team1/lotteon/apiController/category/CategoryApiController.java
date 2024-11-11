package com.team1.lotteon.apiController.category;

import com.team1.lotteon.dto.ResultDTO;
import com.team1.lotteon.dto.category.*;
import com.team1.lotteon.entity.Category;
import com.team1.lotteon.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 카테고리를 관리하는 api controller 생성
*/
@Slf4j
@RestController
@RequestMapping("/api/cate")
@RequiredArgsConstructor
@Transactional
public class CategoryApiController {
    private final CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ResultDTO<List<CategoryWithChildrenResponseDTO>>> getAllCategory() {
        List<CategoryWithChildrenResponseDTO> allCategories = categoryService.getAllRootCategories().getData();
        return ResponseEntity.status(HttpStatus.OK).body(new ResultDTO<>(allCategories));
    }

    // id 값을 이용하여 해당 id값을 parent를 가지는 자식 카테고리 조회 (준혁)
    @GetMapping("/{parentId}/subcategories")
    public ResponseEntity<List<CategoryWithChildrenResponseDTO>> getSubCategories(@PathVariable Long parentId) {
        List<CategoryWithChildrenResponseDTO> subCategories = categoryService.getSubCategoriesByParentId(parentId);
        return ResponseEntity.status(HttpStatus.OK).body(subCategories);
    }

    @GetMapping("/{id}/parents")
    public ResponseEntity<List<CategoryResponseDTO>> getParentList(@PathVariable Long id) {
        List<CategoryResponseDTO> parentList = categoryService.getParentList(id);
        return ResponseEntity.status(HttpStatus.OK).body(parentList);
    }

    // 모든 1차 카테고리 조회 (준혁)
    @GetMapping("/root")
    public ResponseEntity<List<CategoryWithChildrenResponseDTO>> getAllRootCategories() {
        List<CategoryWithChildrenResponseDTO> allCategories = categoryService.getAllRootCategories().getData();
        return ResponseEntity.status(HttpStatus.OK).body(allCategories);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategoryByParentId(@RequestParam("parentId") Long parentId) {
        List<CategoryResponseDTO> result = categoryService.getAllCategoriesByParentId(parentId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryCreateDTO categoryCreateDTO) {
        CategoryResponseDTO categoryResponseDTO = categoryService.createCategory(categoryCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryWithParentAndChildrenResponseDTO> getDetailCategory(@PathVariable Long id) {
        CategoryWithParentAndChildrenResponseDTO detailCategory = categoryService.getDetailById(id);
        return ResponseEntity.status(HttpStatus.OK).body(detailCategory);
    }

    @GetMapping("/{id}/children")
    public ResponseEntity<List<CategoryResponseDTO>> getCategoryChildren(@PathVariable Long id) {
        List<CategoryResponseDTO> categoryChildren = categoryService.getChildrenById(id);
        return ResponseEntity.status(HttpStatus.OK).body(categoryChildren);
    }

    @PatchMapping("/{id}/name")
    public ResponseEntity<Void> updateCateName(@PathVariable Long id, @RequestBody CategoryUpdateNameDTO body) {
        categoryService.updateCategoryName(id, body.getName());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{id}/parent/{parentId}")
    public ResponseEntity<Void> updateCateParent(@PathVariable Long id, @PathVariable Long parentId) {
        categoryService.updateCategoryParent(id, parentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{id}/displayOrder/{targetId}")
    public ResponseEntity<Void> updateCateDisplayOrder(@PathVariable("id") Long id, @PathVariable("targetId") Long targetId) {
        log.info("origin : {} / target : {}", id, targetId);
        categoryService.updateCategoryDisplayOrder(id, targetId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
