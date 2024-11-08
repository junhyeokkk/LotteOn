package com.team1.lotteon.dto.product.productOption;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ModifyRequestProductOptionDTO {
    private String name;        // 옵션명 (예: "색상")
    private String type;        // 옵션 유형 (예: "기본", "입력형", "색상")
    private List<String> values; // 옵션 값 리스트 (예: ["빨강", "파랑", "노랑"])
}