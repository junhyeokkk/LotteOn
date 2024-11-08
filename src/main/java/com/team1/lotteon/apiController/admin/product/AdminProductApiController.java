package com.team1.lotteon.apiController.admin.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.lotteon.dto.product.ProductDTO;
import com.team1.lotteon.dto.product.productOption.ModifyProductOptionCombinationDTO;
import com.team1.lotteon.dto.product.productOption.ModifyRequestProductCombinationDTO;
import com.team1.lotteon.dto.product.productOption.ModifyRequestProductOptionDTO;
import com.team1.lotteon.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/*
    날짜 : 2024/11/6
    이름 : 최준혁
    내용 : ADMIN 상품ㅇ,ㄹ 관리하는 api controller 생성
*/
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/product")
public class AdminProductApiController {

    private final ProductService productService;

    // 상품 수정

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long id,
            @RequestPart("productDTO") ProductDTO productDTO,
            @RequestParam("options") String optionsJson,
            @RequestParam("combinations") String combinationsJson,
            @RequestParam(value = "productImg1", required = false) MultipartFile productImg1,
            @RequestParam(value = "productImg2", required = false) MultipartFile productImg2,
            @RequestParam(value = "productImg3", required = false) MultipartFile productImg3) {
        log.info("입성1!!!!");
        log.info("옵션스" + optionsJson);
        log.info("조합스" + combinationsJson);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // 특수문자 허용
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            // options와 combinations JSON 문자열을 List로 변환
            List<ModifyRequestProductOptionDTO> options = objectMapper.readValue(optionsJson, new TypeReference<List<ModifyRequestProductOptionDTO>>() {});
            List<ModifyRequestProductCombinationDTO> combinations = objectMapper.readValue(combinationsJson, new TypeReference<List<ModifyRequestProductCombinationDTO>>() {});

            log.info("입성2!!!!");
            log.info("옵션!!!" + options.toString());
            log.info("옵션 조합!!!" + combinations.toString());

//          // 서비스 메서드에 제품 정보 및 추가 데이터 전달하여 업데이트
            productService.updateProduct(id, productDTO, options, combinations, productImg1, productImg2, productImg3);

            return ResponseEntity.ok("상품이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("상품 수정에 실패했습니다: " + e.getMessage());
        }
    }
}