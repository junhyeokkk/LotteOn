package com.team1.lotteon.apiController.product;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.product.ProductCreateDTO;
import com.team1.lotteon.dto.product.ProductSummaryResponseDTO;
import com.team1.lotteon.dto.product.productOption.ProductOptionDTO;
import com.team1.lotteon.dto.product.productOption.ProductOptionCombinationDTO;
import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import com.team1.lotteon.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 상품을 관리하는 api controller 생성

    - 수정내역
    - 상품 등록 메서드 수정 (준혁)
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductApiController {
    private static final Logger log = LogManager.getLogger(ProductApiController.class);
    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @PostMapping("/register")
    public ResponseEntity<?> createProduct(
            @ModelAttribute ProductCreateDTO productCreateDTO,
            @RequestParam(value = "file1", required = false) MultipartFile file1,
            @RequestParam(value = "file2", required = false) MultipartFile file2,
            @RequestParam(value = "file3", required = false) MultipartFile file3
    ) {
        log.info("잘 바인딩 되니? " + productCreateDTO.toString());

        try {
            // 파일 업로드 처리 및 저장
            String imgPath1 = file1 != null ? productService.uploadFile(file1) : null;
            String imgPath2 = file2 != null ? productService.uploadFile(file2) : null;
            String imgPath3 = file3 != null ? productService.uploadFile(file3) : null;

            // 이미지 경로를 DTO에 설정
            productCreateDTO.setProductImg1(imgPath1);
            productCreateDTO.setProductImg2(imgPath2);
            productCreateDTO.setProductImg3(imgPath3);

            // 서비스 호출을 통해 제품 생성
            Product createdProduct = productService.saveProduct(productCreateDTO);

            return ResponseEntity.ok(Collections.singletonMap("message", "Product created successfully"));


        } catch (IOException e) {
            throw new RuntimeException("JSON 처리 중 오류 발생: " + e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<PageResponseDTO<ProductSummaryResponseDTO>> getProducts(@PageableDefault Pageable pageable) {
        System.out.println("pageable = " + pageable);
        PageResponseDTO<ProductSummaryResponseDTO> products = productService.getProducts(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }



}
