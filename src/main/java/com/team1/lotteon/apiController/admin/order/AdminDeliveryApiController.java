package com.team1.lotteon.apiController.admin.order;

import com.team1.lotteon.dto.order.AdminOrderSummaryDTO;
import com.team1.lotteon.dto.order.DeliveryDTO;
import com.team1.lotteon.dto.order.DeliveryDetailDTO;
import com.team1.lotteon.dto.order.OrderItemDeilveryDTO;
import com.team1.lotteon.entity.Order;
import com.team1.lotteon.service.Order.DeliveryService;
import com.team1.lotteon.service.admin.AdminOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
    날짜 : 2024/11/4
    이름 : 최준혁
    내용 : ADMIN 배송를 관리하는 api controller 생성
*/
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/delivery")
public class AdminDeliveryApiController {

    private final DeliveryService deliveryService;

    @GetMapping("/detail")
    public ResponseEntity<DeliveryDetailDTO> getDeliveryDetail(@RequestParam Long id) {
        DeliveryDetailDTO deliveryDetails = deliveryService.getDeliveryDetailsById(id);

        log.info("dddd" + deliveryDetails);
        return ResponseEntity.ok(deliveryDetails);
    }

}
