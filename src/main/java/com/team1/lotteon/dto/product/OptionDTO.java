package com.team1.lotteon.dto.product;

import com.team1.lotteon.entity.productOption.OptionGroup;
import lombok.*;

/*
    날짜 : 2024/10/25
    이름 : 최준혁
    내용 : 옵션 DTO 생성 (옵션그룹의 값들 (사이즈 : S ))

*/

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionDTO {


    private Long id;

    private String name;

    private OptionGroup optionGroup;

}
