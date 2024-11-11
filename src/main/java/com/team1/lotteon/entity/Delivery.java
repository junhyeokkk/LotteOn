package com.team1.lotteon.entity;

import com.team1.lotteon.entity.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
/*
    날짜 : 2024/10/31
    이름 : 최준혁
    내용 : 배달 엔티티 생성

*/

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_item_id")  // OrderItem과의 관계 설정
    private OrderItem orderItem;
    
    private String zip; // 우편번호
    private String addr1; // 기본 주소
    private String addr2; // 상세 주소

    private String delCompany; // 택배사
    private String InvoiceNum; // 송장번호
    private String memo;

    private LocalDateTime deliveryDate; // 배송 접수일자

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20) // Enum의 최대 길이에 맞게 조정
    private DeliveryStatus status; // 배송 상태 (예: 준비, 배송 중, 배송 완료)
}
