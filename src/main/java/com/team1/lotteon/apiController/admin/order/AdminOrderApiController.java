package com.team1.lotteon.apiController.admin.order;

import com.team1.lotteon.controller.admin.order.OrderPageController;
import com.team1.lotteon.dto.order.AdminOrderSummaryDTO;
import com.team1.lotteon.dto.order.OrderDTO;
import com.team1.lotteon.dto.order.OrderSummaryDTO;
import com.team1.lotteon.entity.Order;
import com.team1.lotteon.service.admin.AdminOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
    날짜 : 2024/11/2
    이름 : 최준혁
    내용 : ADMIN 오더를 관리하는 api controller 생성
*/
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/order")
public class AdminOrderApiController {

    private final AdminOrderService adminOrderService;

    // 주문내역 상세
    @GetMapping("/detail")
    @ResponseBody
    public ResponseEntity<AdminOrderSummaryDTO> getOrderDetail(@RequestParam Long id) {
        Order order = adminOrderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        AdminOrderSummaryDTO adminOrderSummaryDTO = new AdminOrderSummaryDTO(order);

        return ResponseEntity.ok(adminOrderSummaryDTO);
    }


}
