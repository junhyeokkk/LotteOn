package com.team1.lotteon.service.Order;

import com.team1.lotteon.dto.order.OrderItemDTO;
import com.team1.lotteon.dto.order.OrderRequestDTO;
import com.team1.lotteon.dto.order.OrderSummaryDTO;
import com.team1.lotteon.entity.*;
import com.team1.lotteon.entity.enums.DeliveryStatus;
import com.team1.lotteon.entity.enums.OrderStatus;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import com.team1.lotteon.repository.DeliveryRepoistory;
import com.team1.lotteon.repository.OrderRepository;
import com.team1.lotteon.repository.ProductOptionCombinationRepository;
import com.team1.lotteon.repository.ProductRepository;
import com.team1.lotteon.repository.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
 *   날짜 : 2024/10/31
 *   이름 : 최준혁
 *   내용 : 오더 서비스 생성
 *
 */
@Log4j2
@RequiredArgsConstructor
@Service
public class OrderService {

    // 레포지토리
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductOptionCombinationRepository productOptionCombinationRepository;
    private final DeliveryRepoistory deliveryRepoistory;
    private final CouponRepository couponRepository;

    // 서비스
    private final DeliveryService deliveryService;


    // insert 오더
    public OrderSummaryDTO createOrder(@ModelAttribute OrderRequestDTO request, GeneralMember member) {
        List<OrderItem> orderItems = new ArrayList<>();

        // 1. 주문 생성 (Order 객체 먼저 생성)
        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .orderNumber(UUID.randomUUID().toString())
                .member(member)
//                .usedPoint(request.getUsedPoint()) // 포인트 development
                .usedPoint(0)
//                .coupon(getCouponIfExists(request.getCouponId()))
                .paymentMethod(request.getPaymentMethod())
                .recipientName(request.getRecipientName())
                .recipientPhone(request.getRecipientPhone())
                .recipientZip(request.getRecipientZip())
                .recipientAddr1(request.getRecipientAddr1())
                .recipientAddr2(request.getRecipientAddr2())
                .status(OrderStatus.ORDERED)
                .totalPrice(request.getTotalPrice())
                .build();

        // 2. 각 OrderItemDTO를 통해 주문 항목(OrderItem) 생성 및 Order 설정
        for (OrderItemDTO itemDTO : request.getOrderItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + itemDTO.getProductId()));

            // 옵션 조합 설정
            ProductOptionCombination optionCombination = productOptionCombinationRepository.findById(itemDTO.getProductOptionCombinationId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid combination ID: " + itemDTO.getProductOptionCombinationId()));

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .productOptionCombination(optionCombination)
                    .quantity(itemDTO.getQuantity())
                    .orderPrice(itemDTO.getOrderPrice())
                    .discountRate(product.getDiscountRate())
                    .point((int) (itemDTO.getOrderPrice() * 0.01))  // 예시: 결제 금액의 1% 포인트 적립
                    .deliveryFee(product.getDeliveryFee())
                    .deliveryStatus(DeliveryStatus.READY)
                    .order(order)  // Order 설정
                    .build();

            // 개별 Delivery 생성 및 설정
            Delivery delivery = deliveryService.createDelivery(request, orderItem);
            orderItem.setDelivery(delivery);
            orderItems.add(orderItem);
        }

        // 3. 주문 항목(OrderItems)을 Order에 추가하고 총 배달료 계산
        order.setOrderItems(orderItems);
        order.calculateTotalDeliveryFee();

        // 5. 주문 저장
        orderRepository.save(order);

        // 최종 주문 요약 정보 반환
        return new OrderSummaryDTO(order);
    }

    // complete 페이지 반환할 주문 요약 정보
    public OrderSummaryDTO getOrderSummary(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + orderId));

        return new OrderSummaryDTO(order);
    }
    private Coupon getCouponIfExists(Long couponId) {
        return (couponId != null) ?
                couponRepository.findById(couponId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid coupon ID: " + couponId))
                : null;
    }
}
