package com.team1.lotteon.dto.product.productOption;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ModifyRequestProductOptionItemDTO {
    private Long optionId;  // 연결된 옵션 ID
    private Long id;
    private String value;
}
