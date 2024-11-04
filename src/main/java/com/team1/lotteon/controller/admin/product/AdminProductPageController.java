package com.team1.lotteon.controller.admin.product;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.product.ProductSearchRequestDto;
import com.team1.lotteon.dto.product.ProductSummaryResponseDTO;
import com.team1.lotteon.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
/*
     날짜 : 2024/10/23
     이름 : 최준혁
     내용 : ProductPageController 생성

     수정이력
      - ADMIN => Product 페이지 이동 메서드 생성

*/

@Log4j2
@Controller
@RequiredArgsConstructor
public class AdminProductPageController {
    private final ProductService productService;

    @GetMapping("/admin/product/list")
    public String list(ProductSearchRequestDto requestDto, Model model) {
        PageResponseDTO<ProductSummaryResponseDTO> products = productService.searchProducts(requestDto);
        log.info("프로덕트 " + products);
        model.addAttribute("products", products);
        model.addAttribute("searchDto", requestDto);
        return "admin/product/list";
    }

    @GetMapping("/admin/product/register")
    public String register(){

        return "admin/product/register";
    }
}
