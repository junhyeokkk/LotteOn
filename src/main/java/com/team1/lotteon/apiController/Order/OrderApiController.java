package com.team1.lotteon.apiController.Order;

import com.team1.lotteon.dto.GeneralMemberDTO;
import com.team1.lotteon.dto.PointDTO;
import com.team1.lotteon.dto.order.OrderRequestDTO;
import com.team1.lotteon.dto.order.OrderSummaryDTO;
import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.service.Order.OrderService;
import com.team1.lotteon.service.PointService;
import com.team1.lotteon.service.admin.AdminMemberService;
import com.team1.lotteon.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final PointService pointService;
    private final AdminMemberService adminMemberService;

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



    @PostMapping("/use")
    public ResponseEntity<String> usePoints(@RequestBody PointDTO pointDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자입니다.");
        }

        String uid = ((UserDetails) authentication.getPrincipal()).getUsername();
        GeneralMemberDTO generalMemberDTO = adminMemberService.getMemberByUid(uid); // 회원 정보 가져오기

        if (generalMemberDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 회원입니다.");
        }


        try {
            // 포인트 사용 메서드 호출
            GeneralMember generalMember = new GeneralMember(); // GeneralMemberDTO를 GeneralMember로 변환 (필요한 필드만 변환)
            generalMember.setUid(generalMemberDTO.getUid());
            generalMember.setPoints(generalMemberDTO.getPoints());
            pointService.userOrderPoints(pointDTO.getGivePoints(), generalMember);

            return ResponseEntity.ok("포인트가 성공적으로 사용되었습니다.");
        } catch (IllegalArgumentException e) {
            log.error("포인트 사용 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

