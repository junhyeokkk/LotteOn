package com.team1.lotteon.service.Order;

import com.team1.lotteon.dto.order.*;
import com.team1.lotteon.entity.*;
import com.team1.lotteon.entity.enums.DeliveryStatus;
import com.team1.lotteon.entity.enums.OrderStatus;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import com.team1.lotteon.repository.*;
import com.team1.lotteon.repository.coupon.CouponRepository;
import com.team1.lotteon.service.PointService;
import com.team1.lotteon.service.admin.CouponTakeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*
 *   날짜 : 2024/10/31
 *   이름 : 최준혁
 *   내용 : 오더 서비스 생성
 *
 *   수정사항
 *   - 2024/11/12 이도영 OrderItemPageResponseDTO 페이지 처리 수정
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
    private final OrderItemRepository orderItemRepository;
    private final RefundRepository refundRepository;
    private final ExchangeRepository exchangeRepository;
    private final CouponTakeService couponTakeService;
    private final PointService pointService;

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
                orderItemBuilder.optionCombinationSnapshot(optionCombination.getOptionValueCombination());  // 조합 정보 스냅샷 저장

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

        // 4. 쿠폰 및 포인트 적용
        if(request.getCouponId() != null) {
            couponTakeService.updateCouponUseStatusAndIncrementUse(member, request.getCouponId());
        }
        if(request.getPointDiscount() > 0) {
            pointService.deductPoints(member, request.getPointDiscount(), order);
        }

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

        List<Order> myOrder = orderRepository.findTop5ByMember_UidOrderByOrderDateDesc(uid);

        List<OrderItem> orderItems = myOrder.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .collect(Collectors.toList());

        return orderItems;
    }

    // complete 페이지 반환할 주문 요약 정보
    public OrderSummaryDTO getOrderSummary(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID: " + orderId));

        return new OrderSummaryDTO(order);
    }
    //마이페이지 주문내역 출력(도영)
    public int getOrderCountByUserId(String userId) {
        List<Order> orders = orderRepository.findAllByMember_Uid(userId);
        return orders != null ? orders.size() : 0;
    }
    // 쿠폰 추가 디벨롭
//    private Coupon getCouponIfExists(Long couponId) {
//        return (couponId != null) ?
//                couponRepository.findById(couponId)
//                        .orElseThrow(() -> new IllegalArgumentException("Invalid coupon ID: " + couponId))
//                : null;
//    }

    public OrderItemPageResponseDTO getOrdersByDateRange(String uid, OrderItemPageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable("orderDate");

        // 기본 날짜 설정
        LocalDateTime start = (requestDTO.getStartDate() != null && !requestDTO.getStartDate().isEmpty())
                ? LocalDate.parse(requestDTO.getStartDate()).atStartOfDay()
                : LocalDateTime.now().minusMonths(5); // 기본값: 최근 5개월 전부터

        LocalDateTime end = (requestDTO.getEndDate() != null && !requestDTO.getEndDate().isEmpty())
                ? LocalDate.parse(requestDTO.getEndDate()).atTime(LocalTime.MAX)
                : LocalDateTime.now(); // 기본값: 오늘 날짜

        // 1단계: 조건에 맞는 Order 목록 조회
        Page<Order> orderPage = orderRepository.findByMemberUidAndOrderDateBetween(uid, start, end, pageable);

        // 2단계: 조회된 Order 목록을 기반으로 OrderItem 조회 및 변환
        List<OrderItemDTO> orderItemDTOs = orderPage.getContent().stream()
                .flatMap(order -> order.getOrderItems().stream()) // Order의 OrderItems를 스트림으로 변환
                .map(orderItem -> modelMapper.map(orderItem, OrderItemDTO.class))
                .collect(Collectors.toList());

        return new OrderItemPageResponseDTO(requestDTO, orderItemDTOs, (int) orderPage.getTotalElements());
    }


    // 오더 아이템 수취확인
    @Transactional
    public boolean updateDeliveryStatus(Long orderItemId) {
        return orderItemRepository.findById(orderItemId).map(orderItem -> {
            // 1. 해당 OrderItem의 상태를 COMPLETE로 설정
            orderItem.getDelivery().setStatus(DeliveryStatus.COMPLETE);
            orderItem.setDeliveryStatus(DeliveryStatus.COMPLETE);
            orderItemRepository.save(orderItem); // 변경 사항 저장

            log.info("1111");
            // 2. 주문 ID 가져오기
            Long orderId = orderItem.getOrder().getId();

            log.info("2222");
            log.info("dddddd" + orderId);
            // 3. 해당 주문의 모든 OrderItem이 COMPLETE 상태인지 확인
            boolean allItemsComplete = orderItemRepository
                    .findByOrder_Id(orderId)
                    .stream()
                    .allMatch(item -> item.getDeliveryStatus() == DeliveryStatus.COMPLETE);

            log.info("3333");
            log.info("4444"+ allItemsComplete);
            // 4. 모든 OrderItem이 COMPLETE이면 주문 상태를 COMPLETE로 변경
            if (allItemsComplete) {
                orderItem.getOrder().setStatus(OrderStatus.COMPLETE);
                orderRepository.save(orderItem.getOrder());
            }
            return true;
        }).orElse(false); // orderItem을 찾지 못한 경우 false 반환
    }

    @Transactional
    public boolean requestRefund(Long orderItemId, String refundReason, String reasonText, MultipartFile imageFile) {
        return orderItemRepository.findById(orderItemId).map(orderItem -> {
            // OrderItem 상태를 RETURNREQ로 설정
            orderItem.setDeliveryStatus(DeliveryStatus.valueOf("REFUNDREQ"));
            orderItemRepository.save(orderItem);

            // Refund 엔티티 생성 및 필드 설정
            Refund refund = new Refund();
            refund.setOrderItem(orderItem);
            refund.setRefundReason(refundReason);
            refund.setReasonText(reasonText);

            // 이미지 파일 저장 및 경로 설정
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = saveImageFile(imageFile);
                refund.setImagePath(imagePath);
            }

            refundRepository.save(refund); // Refund 정보 저장
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean requestReturn(Long orderItemId, String returnReason, String reasonText, MultipartFile imageFile) {
        return orderItemRepository.findById(orderItemId).map(orderItem -> {
            // OrderItem 상태를 RETURNREQ로 설정
            orderItem.setDeliveryStatus(DeliveryStatus.valueOf("RETURNREQ"));
            orderItemRepository.save(orderItem);

            // Refund 엔티티 생성 및 필드 설정
            Exchange p_exchange = new Exchange();
            p_exchange.setOrderItem(orderItem);
            p_exchange.setReturnReason(returnReason);
            p_exchange.setReasonText(reasonText);

            // 이미지 파일 저장 및 경로 설정
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = saveImageFile(imageFile);
                p_exchange.setImagePath(imagePath);
            }

            exchangeRepository.save(p_exchange); // Return 정보 저장
            return true;
        }).orElse(false);
    }

    // 이미지 파일 저장 메서드
    private String saveImageFile(MultipartFile imageFile) {
        try {
            // 파일 저장 경로 설정 (예: "/uploads/" 디렉토리)
            String uploadDir = "uploads/";
            String fileName = imageFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);

            // 디렉토리가 없으면 생성
            Files.createDirectories(filePath.getParent());

            // 파일 저장
            Files.write(filePath, imageFile.getBytes());
            return filePath.toString(); // 저장된 파일 경로 반환
        } catch (IOException e) {
            throw new RuntimeException("이미지 파일 저장 중 오류 발생", e);
        }
    }

}