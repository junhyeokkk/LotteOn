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


    // JSON 또는 조합 정보를 저장할 필드
    @Column(columnDefinition = "TEXT")
    private String combinationSnapshot;

    @Column(columnDefinition = "json")
    private String optionCombinationValues; // 기존 조합 값 저장 (JSON)

    private int version; // SKU 버전

    private LocalDateTime createdAt; // 생성된 시간 기록

    private boolean isActive; // 유효 여부 플래그
}