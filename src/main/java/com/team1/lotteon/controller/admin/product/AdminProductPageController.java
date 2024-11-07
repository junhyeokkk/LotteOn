package com.team1.lotteon.controller.admin.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.stream.Collectors;
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
    @GetMapping("/admin/product/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        ProductDTO productDTO = productService.getProductById(id);

        log.info("콤비네이션~~~~~~~~~~~~~~~" + productDTO.getProductOptionCombinations().toString());

        // 옵션 조합 데이터를 보기 좋은 형식으로 가공
        productDTO.getProductOptionCombinations().forEach(combination -> {
            String formattedCombination = parseOptionCombination(combination.getOptionValueCombination());
            combination.setFormattedOptionValueCombination(formattedCombination); // 새로운 필드에 가공된 데이터 저장
        });

        model.addAttribute("product", productDTO);
        log.info("Product details for editing: " + productDTO);
        return "admin/product/edit";
    }

    // JSON 문자열을 "옵션명: 옵션값" 형식으로 변환하는 헬퍼 메서드
    private String parseOptionCombination(String optionValueCombination) {
        try {
            // JSON 문자열을 Map 형태로 파싱
            Map<String, String> combinationMap = new ObjectMapper().readValue(optionValueCombination, new TypeReference<>() {});
            // 각 항목을 "옵션명: 옵션값" 형식으로 변환 후, 콤마로 구분
            return combinationMap.entrySet().stream()
                    .map(entry -> entry.getKey() + ": " + entry.getValue())
                    .collect(Collectors.joining(", "));
        } catch (JsonProcessingException e) {
            log.error("Error parsing option combination JSON", e);
            return optionValueCombination; // 파싱 실패 시 원본 JSON 문자열 반환
        }
    }
}
