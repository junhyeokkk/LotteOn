package com.team1.lotteon.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.team1.lotteon.dto.product.productOption.ProductOptionDTO;
import com.team1.lotteon.dto.product.productOption.ProductOptionCombinationDTO;
import com.team1.lotteon.entity.Productdetail;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 상품 생성을 위한 요청 DTO 생성

    - 수정 내역
    - 이미지, 카테고리 추가 삽입을 위한 DTO 변경 (준혁)
*/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateDTO {

    // 카테고리 정보
    private Long categoryId;

    // 기본 정보
    private String productName;
    private String description;
    private String manufacturer;
    private int price;
    private Integer discountRate;
    private Integer point;
    private int stock;
    private Integer deliveryFee;
    private boolean hasOptions; // 옵션 여부 필드

    // 이미지 파일 (이미지 업로드 처리를 위한 MultipartFile 필드)
    private MultipartFile file1;
    private MultipartFile file2;
    private MultipartFile file3;

    private List<Productdetail> productDetails;  // 상품 상세정보 테이블을 활용해 별도 관리

    private String productImg1; // 상품 이미지1
    private String productImg2; // 상품 이미지2
    private String productImg3; // 상품 이미지3

    // 전자상거래 상품 정보
    private String productStatus;
    private String warranty;
    private String receiptMethod;
    private String businessType;
    private String origin;

    // JSON 문자열로 받기 위한 필드
    private String optionsJson;
    private String combinationsJson;

    // 상세 정보 JSON 문자열로 받기 위한 필드
    private String productDetailsJson;

    // 상품 선택정보 옵션
    @JsonProperty("optionsJson")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<ProductOptionDTO> options;

    @JsonProperty("combinationsJson")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<ProductOptionCombinationDTO> combinations;
}

