package com.team1.lotteon.dto.product;

import com.team1.lotteon.entity.Category;
import jakarta.persistence.*;
import lombok.*;

/*
    날짜 : 2024/10/25
    이름 : 최준혁
    내용 : 옵션 그룹 DTO 생성 ('사이즈', '색상', '재질'같은 그룹)

*/

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionGroupDTO {

    private Long id;

    private String name;

    private Category category;
}
