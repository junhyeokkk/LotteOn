package com.team1.lotteon.controller;

import com.team1.lotteon.dto.category.CategoryWithChildrenResponseDTO;
import com.team1.lotteon.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final CategoryService categoryService;

    @GetMapping
    public String main(Model model) {
        List<CategoryWithChildrenResponseDTO> allCategories = categoryService.getAllRootCategories();
        model.addAttribute("categories", allCategories);
        return "main/layout/main_layout";
    }
}
