package com.team1.lotteon.apiController.Order;

import com.team1.lotteon.dto.order.OrderRequestDTO;
import com.team1.lotteon.dto.order.OrderSummaryDTO;
import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.service.Order.OrderService;
import com.team1.lotteon.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
    날짜 : 2024/10/31
    이름 : 최준혁
    내용 : 오더를 관리하는 api controller 생성
*/
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderApiController {

    private final OrderService orderService;

    // 주문 생성 엔드포인트
    @PostMapping("/create")
    public ResponseEntity<OrderSummaryDTO> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        log.info("Order request 잘 들어오나? " + orderRequest.toString());

        log.info("Order item 잘 들어오나? " + orderRequest.getOrderItems().toString());

        // 로그인된 사용자 확인 및 주문 생성
        GeneralMember member = MemberUtil.getLoggedInGeneralMember();
        if (member == null) {
            log.info("로그인사용자 못찾아모");
            return ResponseEntity.badRequest().build(); // 로그인 필수 처리
        }

        log.info("찾아온 uid" + member.getUid());

        // 주문 생성 서비스 호출
        OrderSummaryDTO orderSummary = orderService.createOrder(orderRequest, member);

        log.info("주문 요약 정보" + orderSummary );

        // 주문 요약 정보를 포함한 응답 반환
        return ResponseEntity.ok(orderSummary);
    }
}
