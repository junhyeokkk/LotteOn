package com.team1.lotteon.service.Order;

import com.team1.lotteon.dto.order.DeliveryDTO;
import com.team1.lotteon.dto.order.DeliveryDetailDTO;
import com.team1.lotteon.dto.order.OrderRequestDTO;
import com.team1.lotteon.entity.Delivery;
import com.team1.lotteon.entity.Order;
import com.team1.lotteon.entity.OrderItem;
import com.team1.lotteon.entity.enums.DeliveryStatus;
import com.team1.lotteon.entity.enums.OrderStatus;
import com.team1.lotteon.repository.DeliveryRepoistory;
import com.team1.lotteon.repository.OrderItemRepository;
import com.team1.lotteon.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
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
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;

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

    // 배송 등록 시 택배 정보 등록 및 배송 상태 변경
    public void registerDelivery(DeliveryDTO deliveryDTO) {
        // OrderItem을 가져와서 존재 여부 확인
        OrderItem orderItem = orderItemRepository.findById(deliveryDTO.getOrderItemId())
                .orElseThrow(() -> new IllegalArgumentException("해당 주문 항목을 찾을 수 없습니다."));
        // orderitem을 통해 delivery 가져와 택배사 정보 입력 및 배달상태 변경
        Delivery delivery = deliveryRepoistory.findByorderItem(orderItem);
        delivery.setDelCompany(deliveryDTO.getDelCompany());
        delivery.setInvoiceNum(deliveryDTO.getInvoiceNum());
        delivery.setMemo(deliveryDTO.getMemo());
        delivery.setDeliveryDate(LocalDateTime.now());
        delivery.setStatus(DeliveryStatus.DELIVERED);

        log.info("dsdsafsdf" + delivery.toString());
        deliveryRepoistory.save(delivery);

        // OrderItem 상태 업데이트
        orderItem.setDelivery(delivery);
        orderItem.setDeliveryStatus(DeliveryStatus.DELIVERED);

        // 하나의 orderitem이라도 배송 시작했으면 Order 상태 deliverd로 change
        orderItem.getOrder().setStatus(OrderStatus.DELIVERING);

        orderItemRepository.save(orderItem);
    }

    // delivery 상세정보를 위한 delivery select
    @Transactional
    public DeliveryDetailDTO getDeliveryDetailsById(Long deliveryId) {

        Optional<Delivery> deliveryOpt = deliveryRepoistory.findById(Math.toIntExact(deliveryId));

        if (deliveryOpt.isPresent()) {
            Delivery delivery = deliveryOpt.get();
            return new DeliveryDetailDTO(delivery);
        } else {
            throw new IllegalArgumentException("해당 배송 정보가 존재하지 않습니다.");
        }
    }

    // 배송 완료처리
    @Transactional
    public boolean completeDelivery(Long deliveryId) {
        return deliveryRepoistory.findById(Math.toIntExact(deliveryId)).map(delivery -> {
            delivery.setStatus(DeliveryStatus.SHIPPED); // 배송완료 상태로 설정 (DB에 맞는 값 사용)
            delivery.getOrderItem().setDeliveryStatus(DeliveryStatus.SHIPPED);
            deliveryRepoistory.save(delivery);
            return true;
        }).orElse(false);
    }
}
