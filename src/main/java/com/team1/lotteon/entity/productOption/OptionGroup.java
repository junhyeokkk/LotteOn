package com.team1.lotteon.entity.productOption;

import com.team1.lotteon.entity.Category;
import jakarta.persistence.*;
import lombok.*;

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
@Table(name = "option_group")
public class OptionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;
}
