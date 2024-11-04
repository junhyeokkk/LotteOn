package com.team1.lotteon.controller.admin.order;

import com.team1.lotteon.dto.Member.MemberPageRequestDTO;
import com.team1.lotteon.dto.Member.MemberPageResponseDTO;
import com.team1.lotteon.dto.order.DeliveryDTO;
import com.team1.lotteon.dto.order.OrderPageRequestDTO;
import com.team1.lotteon.dto.order.OrderPageResponseDTO;
import com.team1.lotteon.dto.order.OrderRequestDTO;
import com.team1.lotteon.dto.point.PointPageRequestDTO;
import com.team1.lotteon.dto.point.PointPageResponseDTO;
import com.team1.lotteon.entity.SellerMember;
import com.team1.lotteon.security.MyUserDetails;
import com.team1.lotteon.service.Order.OrderService;
import com.team1.lotteon.service.admin.AdminDeliveryService;
import com.team1.lotteon.service.admin.AdminOrderService;
import com.team1.lotteon.util.DateUtil;
import com.team1.lotteon.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

/*
     날짜 : 2024/10/23
     이름 : 최준혁
     내용 : 관리자 주문 컨트롤러 생성

*/
@Log4j2
@RequiredArgsConstructor
@Controller
public class OrderPageController {

    private final AdminOrderService adminOrderService;
    private final DateUtil dateUtil;
    private final AdminDeliveryService adminDeliveryService;


    @GetMapping("/admin/order/delivery")
    public String getDeliveryList(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  Model model) {
        // 로그인한 사용자 불러오기
        Object loggedInUser = MemberUtil.getLoggedInMember(); // MemberUtil을 통해 사용자 조회
        Page<DeliveryDTO> deliveryPage = adminDeliveryService.getDeliveryListByUserRole(loggedInUser, PageRequest.of(page, size));

        // 포맷팅된 날짜 추가
        deliveryPage.getContent().forEach(delivery -> {
            if(delivery.getDeliveryDate() != null){
                delivery.setDeliveryDate(dateUtil.formatLocalDateTime(LocalDateTime.parse(delivery.getDeliveryDate())));
            }
        });


        model.addAttribute("deliveries", deliveryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", deliveryPage.getTotalPages());
        return "admin/order/delivery"; // Thymeleaf 템플릿
    }

    @GetMapping("/admin/order/list")
    public String list(
            @RequestParam(defaultValue = "1") int pg,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            Model model,  @AuthenticationPrincipal MyUserDetails myUserDetails) {
        // 로그 추가: 타입과 키워드 출력
        log.info("타입: " + type);
        log.info("키워드: " + keyword);

        // DTO 생성
        OrderPageRequestDTO requestDTO = OrderPageRequestDTO.builder()
                .pg(pg)
                .size(10)
                .type(type) // 타입 추가
                .keyword(keyword) // 키워드 추가
                .build();

        // 로그인 유저 불러오기 (seller, admin 구분)
        SellerMember member = myUserDetails.getSellerMember();

        // 포인트 데이터 가져오기
        OrderPageResponseDTO responseDTO = adminOrderService.getOrders(requestDTO, member);
        model.addAttribute("orders", responseDTO);

//        // 포맷팅된 날짜 추가
//        responseDTO.getDtoList().forEach(order -> {
//            order.setOrderDate(dateUtil.formatLocalDateTime(order.getOrderDate()));
//        });

        // 로그: DTO 리스트 출력
        log.info("오더 데이터: " + responseDTO.getDtoList());

        return "admin/order/list";
    }
}
