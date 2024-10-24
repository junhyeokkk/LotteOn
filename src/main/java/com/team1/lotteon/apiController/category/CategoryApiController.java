package com.team1.lotteon.apiController.category;

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

@Slf4j
@RestController
@RequestMapping("/api/cate")
@RequiredArgsConstructor
@Transactional
public class CategoryApiController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryWithChildrenResponseDTO>> getAllCategory() {
        List<CategoryWithChildrenResponseDTO> allCategories = categoryService.getAllRootCategories();
        return ResponseEntity.status(HttpStatus.OK).body(allCategories);
    }

    @PostMapping
    public ResponseEntity<CategoryIdResponseDTO> createCategory(@RequestBody CategoryCreateDTO categoryCreateDTO) {
        Category category = categoryService.createCategory(categoryCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CategoryIdResponseDTO(category.getId().toString()));
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
