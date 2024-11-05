package com.team1.lotteon.controller.admin.product;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.product.ProductDTO;
import com.team1.lotteon.dto.product.ProductSearchRequestDto;
import com.team1.lotteon.dto.product.ProductSummaryResponseDTO;
import com.team1.lotteon.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @GetMapping("/admin/product/edit")
    public String edit(){

        return "admin/product/edit";
    }

    // 상품 수정 페이지로 이동
    @GetMapping("/admin/product/edit/{productId}")
    public String showEditProductForm(@PathVariable Long productId, Model model) {
        ProductDTO productDTO = productService.getProductById(productId);
        model.addAttribute("product", productDTO);
        log.info("sfaddddddddddddddddddddd" + productDTO);
        return "admin/product/edit";
    }

    // 상품 수정 처리
    @PostMapping("/admin/product/update")
    public String updateProduct(@ModelAttribute ProductDTO productDTO,
                                @RequestParam(value = "productImg1", required = false) MultipartFile productImg1,
                                @RequestParam(value = "productImg2", required = false) MultipartFile productImg2,
                                @RequestParam(value = "productImg3", required = false) MultipartFile productImg3) {

        if (!productImg1.isEmpty()) productDTO.setProductImg1(productImg1.getOriginalFilename());
        if (!productImg2.isEmpty()) productDTO.setProductImg2(productImg2.getOriginalFilename());
        if (!productImg3.isEmpty()) productDTO.setProductImg3(productImg3.getOriginalFilename());

        productService.updateProduct(productDTO);
        return "redirect:/admin/product/list";
    }
}
