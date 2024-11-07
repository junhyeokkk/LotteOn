package com.team1.lotteon.entity.productOption;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/*
    날짜 : 2024/10/25
    이름 : 최준혁
    내용 : 옵션 그룹 엔티티 생성 ('사이즈', '색상', '재질'같은 그룹)

*/

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "option_item")
public class OptionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value; // 옵션 값 예: 빨강, S 등

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    private ProductOption productOption; // 속한 옵션 그룹 예: 색상, 사이즈

    // value와 productOption을 설정하는 생성자
    public OptionItem(String value, ProductOption productOption) {
        this.value = value;
        this.productOption = productOption;
    }

}
