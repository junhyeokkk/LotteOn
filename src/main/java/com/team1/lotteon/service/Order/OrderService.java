package com.team1.lotteon.service.Order;

import com.team1.lotteon.dto.order.OrderDTO;
import com.team1.lotteon.dto.order.OrderItemDTO;
import com.team1.lotteon.dto.order.OrderRequestDTO;
import com.team1.lotteon.dto.order.OrderSummaryDTO;
import com.team1.lotteon.entity.*;
import com.team1.lotteon.entity.enums.DeliveryStatus;
import com.team1.lotteon.entity.enums.OrderStatus;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import com.team1.lotteon.repository.*;
import com.team1.lotteon.repository.coupon.CouponRepository;
import com.team1.lotteon.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final ModelMapper modelMapper;

    // 서비스
    private final DeliveryService deliveryService;
    private final CartRepository cartRepository;

    // insert 오더
    public OrderSummaryDTO createOrder(@ModelAttribute OrderRequestDTO request, GeneralMember member) {
        List<OrderItem> orderItems = new ArrayList<>();

        // 1. 주문 생성 (Order 객체 먼저 생성)
        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .orderNumber(UUID.randomUUID().toString())
                .member(member)
                .usedPoint(0)  // 포인트 초기화 (필요 시 개발)
                .paymentMethod(request.getPaymentMethod())
                .recipientName(request.getRecipientName())
                .recipientPhone(request.getRecipientPhone())
                .recipientZip(request.getRecipientZip())
                .recipientAddr1(request.getRecipientAddr1())
                .recipientAddr2(request.getRecipientAddr2())
                .status(OrderStatus.ORDERED)
                .totalPrice(request.getTotalPrice())
                .couponDiscount(request.getCouponDiscount())
                .pointDiscount(request.getPointDiscount())
                .build();

        // 2. 각 OrderItemDTO를 통해 주문 항목(OrderItem) 생성 및 Order 설정
        for (OrderItemDTO itemDTO : request.getOrderItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + itemDTO.getProductId()));

            OrderItem.OrderItemBuilder orderItemBuilder = OrderItem.builder()
                    .product(product)
                    .quantity(itemDTO.getQuantity())
                    .orderPrice(itemDTO.getOrderPrice())
                    .discountRate(product.getDiscountRate())
                    .point((int) (itemDTO.getOrderPrice() * 0.01))  // 예시: 결제 금액의 1% 포인트 적립
                    .deliveryFee(product.getDeliveryFee())
                    .deliveryStatus(DeliveryStatus.READY)
                    .order(order);  // Order 설정

            if (itemDTO.getProductOptionCombinationId() != null) {
                // 옵션 조합이 있는 상품
                ProductOptionCombination optionCombination = productOptionCombinationRepository.findById(itemDTO.getProductOptionCombinationId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid combination ID: " + itemDTO.getProductOptionCombinationId()));
                orderItemBuilder.productOptionCombination(optionCombination);

                // 옵션 조합의 재고 업데이트
                updateCombinationStock(optionCombination, itemDTO.getQuantity());
            } else {
                // 옵션이 없는 상품 재고 업데이트
                updateProductStock(product, itemDTO.getQuantity());
            }

            // OrderItem 빌더로 객체 생성
            OrderItem orderItem = orderItemBuilder.build();

            // 개별 Delivery 생성 및 설정
            Delivery delivery = deliveryService.createDelivery(request, orderItem);
            orderItem.setDelivery(delivery);
            orderItems.add(orderItem);

            // 주문 완료 후 cartId가 있는 경우 카트에서 삭제
            if (itemDTO.getCartId() != null) {
                cartRepository.deleteById(Math.toIntExact(itemDTO.getCartId()));
            }
        }

        // 3. 주문 항목(OrderItems)을 Order에 추가하고 총 배달료 계산
        order.setOrderItems(orderItems);
        order.calculateTotalDeliveryFee();

        // 5. 주문 저장
        orderRepository.save(order);

        // 최종 주문 요약 정보 반환
        return new OrderSummaryDTO(order);
    }

    // 옵션 조합의 재고 업데이트 메서드
    private void updateCombinationStock(ProductOptionCombination optionCombination, int quantity) {
        if (optionCombination.getStock() < quantity) {
            throw new IllegalArgumentException("선택한 옵션 조합의 재고가 부족합니다.");
        }

        // 조합 재고 감소
        optionCombination.setStock(optionCombination.getStock() - quantity);
        productOptionCombinationRepository.save(optionCombination);

        // 해당 상품의 모든 옵션 조합 재고 합산 후 Product의 재고 업데이트
        Product product = optionCombination.getProduct();
        int totalStock = productOptionCombinationRepository.findByProductId(product.getId())
                .stream().mapToInt(ProductOptionCombination::getStock).sum();
        product.setStock(totalStock);
        productRepository.save(product);
    }

    // 옵션 없는 상품의 재고 업데이트 메서드
    private void updateProductStock(Product product, int quantity) {
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("해당 상품의 재고가 부족합니다.");
        }

        // 재고 감소
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

    // Mypage 메인에 띄울 가장 최근 Orderitem 들고오기
    public List<OrderItem> getMyOrder(String uid){

        Order myOrder = orderRepository.findTop1ByMember_UidOrderByOrderDateDesc(uid);
        OrderDTO orderDTO = modelMapper.map(myOrder, OrderDTO.class);

        List<OrderItem> orderItems = orderDTO.getOrderItems();

        return orderItems;
    }

    // complete 페이지 반환할 주문 요약 정보
    public OrderSummaryDTO getOrderSummary(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + orderId));

        return new OrderSummaryDTO(order);
    }
    // 쿠폰 추가 디벨롭
//    private Coupon getCouponIfExists(Long couponId) {
//        return (couponId != null) ?
//                couponRepository.findById(couponId)
//                        .orElseThrow(() -> new IllegalArgumentException("Invalid coupon ID: " + couponId))
//                : null;
//    }
}
