package com.team1.lotteon.service.Order;

import com.team1.lotteon.dto.order.OrderRequestDTO;
import com.team1.lotteon.entity.Delivery;
import com.team1.lotteon.entity.Order;
import com.team1.lotteon.entity.OrderItem;
import com.team1.lotteon.entity.enums.DeliveryStatus;
import com.team1.lotteon.repository.DeliveryRepoistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
/*
 *   날짜 : 2024/10/31
 *   이름 : 최준혁
 *   내용 : 배달 서비스 생성
 *
 */

@Log4j2
@RequiredArgsConstructor
@Service
public class DeliveryService {
    private final DeliveryRepoistory deliveryRepoistory;

    // 배달 추가 메서드

    public Delivery createDelivery(OrderRequestDTO request, OrderItem orderItem) {
        return Delivery.builder()
                .orderItem(orderItem)  // Order 설정
                .zip(request.getRecipientZip())
                .addr1(request.getRecipientAddr1())
                .addr2(request.getRecipientAddr2())
                .status(DeliveryStatus.READY)  // 초기 상태
                .build();
    }
}
