package com.team1.lotteon.controller.admin.product;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.product.ProductCreateDTO;
import com.team1.lotteon.dto.product.ProductSummaryResponseDTO;
import com.team1.lotteon.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProductPageController {
    private final ProductService productService;

    @GetMapping("/admin/product/list")
    public String list(@PageableDefault Pageable pageable, Model model) {
        PageResponseDTO<ProductSummaryResponseDTO> products = productService.getProducts(pageable);
        model.addAttribute("products", products);
        return "admin/product/list";
    }

    @GetMapping("/admin/product/register")
    public String register(){

        return "admin/product/register";
    }

    @PostMapping("/admin/product/register")
    public String registerPost(@ModelAttribute ProductCreateDTO productCreateDTO){
        productService.createProduct(productCreateDTO);
        return "redirect:/admin/product/list";
    }
}
