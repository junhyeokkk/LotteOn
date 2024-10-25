package com.team1.lotteon.entity.productOption;

import com.team1.lotteon.entity.Category;
import jakarta.persistence.*;
import lombok.*;

/*
    날짜 : 2024/10/25
    이름 : 최준혁
    내용 : 옵션 엔티티 생성 (옵션그룹의 값들 (사이즈 : S ))

*/

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "option")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "optionGroupId", nullable = false)
    private OptionGroup optionGroup;

}
