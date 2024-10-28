package com.team1.lotteon.controller.product;

import com.team1.lotteon.dto.ConfigDTO;
import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.product.ProductDTO;
import com.team1.lotteon.dto.product.ProductSummaryResponseDTO;
import com.team1.lotteon.entity.Product;
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

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 상품 관련 페이지 컨트롤러 생성
*/
@Log4j2
@Controller
@RequiredArgsConstructor
public class ProductPageController {
    private final UserService userService;
    private final ProductService productService;

    // list 페이지 이동
    @GetMapping("/product/list")
    public String list(Model model,  @PageableDefault Pageable pageable){

        log.info("list");
        PageResponseDTO<ProductSummaryResponseDTO> productsPage = productService.getProducts(pageable);
        model.addAttribute("productsPage", productsPage);
        return "product/list";
    }

    // view 페이지 이동
    @GetMapping("/product/view")
    public String view(Model model){

        log.info("view");

        return "product/view";
    }

//    // view 페이지 이동
    @GetMapping("/product/view/{id}")
    public String viewProduct(@PathVariable("id") Long id, Model model) {

        log.info("컨트롤러 ㅇㅇㅇ");
        ProductDTO saveproduct = productService.getProductById(id);

        log.info("찾아온 상품" + saveproduct.toString());
        if (saveproduct != null) {
            model.addAttribute("product", saveproduct); // 모델에 조회한 product 추가
            return "product/view"; // 뷰 페이지로 이동 (ex: view.html)
        } else {
            return "error/404"; // product가 없는 경우, 404 페이지로 이동
        }
    }


//    @GetMapping("/product/{cate}")
//    public String index(@PathVariable String cate, Model model, @PageableDefault Pageable pageable) {
//
//        log.info("product/{} 접속", cate);
//        log.info(cate);
//        model.addAttribute("cate", cate);
//
//        if(cate != null && cate.equals("list"))
//        {
//            PageResponseDTO<ProductSummaryResponseDTO> productsPage = productService.getProducts(pageable);
//            model.addAttribute("productsPage", productsPage);
//        }
//
//        return "product/layout/product_layout";
//    }


}
