package com.team1.lotteon.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Builder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class Point extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;    // 구분
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name="order_id")
    private Order order;
    private int grantedPoints;  // 지급 포인트
    private int accumuatedPoints;   // 누적 포인트
    private String notes;   // 비고
    private LocalDateTime expirationDate;   // 유효기간
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name="member_id")
    private Member member;
}
