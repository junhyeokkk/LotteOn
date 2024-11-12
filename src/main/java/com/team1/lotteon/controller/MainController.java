package com.team1.lotteon.controller;

import com.team1.lotteon.dto.category.CategoryWithChildrenResponseDTO;
import com.team1.lotteon.entity.Product;
import com.team1.lotteon.service.CategoryService;
import com.team1.lotteon.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping
    public String main(Model model) {

        List<CategoryWithChildrenResponseDTO> allCategories = categoryService.getAllRootCategories().getData();

        //히트 상품 조회수 가장 많은 순
        List<Product> producthit = productService.getMainproductsBybesthit();

        //추천 상품 리뷰가 가장 많으면서 평점이 높은 순서대로
        List<Product> productreview = productService.getTopProductsByReviewCountAndScore();

        //최신상품 가장 최근에 등록된 상품 순서 대로
        List<Product> productresent = productService.getLatestProducts();

        //인기 상품 구매율이 가장 높으면서 평점이 높은순
        List<Product> productbestofbest = productService.getTopSellingAndRatedProducts();

        //할인상품 할인율이 가장 많은 순서
        List<Product> productbestdiscount =productService.getTopDiscountedProducts();

        model.addAttribute("categories", allCategories);
        model.addAttribute("producthits", producthit);
        model.addAttribute("productreviews", productreview);
        model.addAttribute("productresents", productresent);
        model.addAttribute("productbestofbest", productbestofbest);
        model.addAttribute("productbestdiscount", productbestdiscount);

        return "main/layout/main_layout";

    }
}
