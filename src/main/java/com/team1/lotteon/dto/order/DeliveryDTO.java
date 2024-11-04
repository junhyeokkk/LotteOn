package com.team1.lotteon.dto.order;

import com.team1.lotteon.entity.Order;
import com.team1.lotteon.entity.OrderItem;
import com.team1.lotteon.entity.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {

    private Long id;

    private Order order;

    private Long OrderItemId;

    private OrderItem orderItem;

    private String zip; // 우편번호
    private String addr1; // 기본 주소
    private String addr2; // 상세 주소

    private String delCompany; // 택배사
    private String invoiceNum; // 송장번호
    private String memo; // 메모

    private String deliveryDate; // 배송 접수일자

    private DeliveryStatus status; // 배송 상태 (예: 준비, 배송 중, 배송 완료)
}
