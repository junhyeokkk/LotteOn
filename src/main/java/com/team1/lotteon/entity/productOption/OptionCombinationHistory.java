package com.team1.lotteon.entity.productOption;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "option_combination_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionCombinationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combination_id")
    private ProductOptionCombination productOptionCombination;

    @Column(columnDefinition = "json")
    private String optionCombinationValues; // 기존 조합 값 저장 (JSON)

    private int version; // SKU 버전

    private LocalDateTime createdAt; // 생성된 시간 기록

    private boolean isActive; // 유효 여부 플래그
}