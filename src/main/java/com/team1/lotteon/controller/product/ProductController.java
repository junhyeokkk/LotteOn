package com.team1.lotteon.controller.product;

import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.product.ProductSummaryResponseDTO;
import com.team1.lotteon.service.ProductService;
import com.team1.lotteon.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Log4j2
@Controller
@RequiredArgsConstructor
public class ProductController {
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/product/{cate}")
    public String index(@PathVariable String cate, Model model, @PageableDefault Pageable pageable) {

        log.info("product/{} 접속", cate);
        log.info(cate);
        model.addAttribute("cate", cate);

        if(cate != null && cate.equals("list"))
        {
            PageResponseDTO<ProductSummaryResponseDTO> productsPage = productService.getProducts(pageable);
            model.addAttribute("productsPage", productsPage);
        }

        return "product/layout/product_layout";
    }
}
