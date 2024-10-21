package com.team1.lotteon.apiController.category;

import com.team1.lotteon.dto.category.CategoryCreateDTO;
import com.team1.lotteon.dto.category.CategoryIdResponseDTO;
import com.team1.lotteon.dto.category.CategoryResponseDTO;
import com.team1.lotteon.dto.category.CategoryWithChildrenResponseDTO;
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
        List<CategoryWithChildrenResponseDTO> allCategories = categoryService.getAllCategories();
        log.info("all cate size : {}", allCategories.size());
        return ResponseEntity.status(HttpStatus.OK).body(allCategories);
    }

    @PostMapping
    public ResponseEntity<CategoryIdResponseDTO> createCategory(@RequestBody CategoryCreateDTO categoryCreateDTO) {
        Category category = categoryService.createCategory(categoryCreateDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CategoryIdResponseDTO(category.getId().toString()));
    }


}
