package com.team1.lotteon.apiController.admin.order;

import com.team1.lotteon.dto.order.*;
import com.team1.lotteon.entity.Order;
import com.team1.lotteon.repository.query.AdminQueryRepository;
import com.team1.lotteon.repository.query.dto.OperatingStatusQueryDTO;
import com.team1.lotteon.repository.query.dto.OrderDailyQueryDTO;
import com.team1.lotteon.repository.query.dto.OrderItemSalesRatioQueryDTO;
import com.team1.lotteon.service.Order.DeliveryService;
import com.team1.lotteon.service.admin.AdminOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/*
    날짜 : 2024/11/2
    이름 : 최준혁
    내용 : ADMIN 오더를 관리하는 api controller 생성
*/
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/order")
public class AdminOrderApiController {

    private final AdminOrderService adminOrderService;
    private final DeliveryService deliveryService;
    private final AdminQueryRepository adminQueryRepository;

    // 주문내역 상세
    @GetMapping("/detail")
    @ResponseBody
    public ResponseEntity<AdminOrderSummaryDTO> getOrderDetail(@RequestParam Long id) {
        Order order = adminOrderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        AdminOrderSummaryDTO adminOrderSummaryDTO = new AdminOrderSummaryDTO(order);

        log.info("afsddddddddddddddd" + adminOrderSummaryDTO.toString());
        return ResponseEntity.ok(adminOrderSummaryDTO);
    }

    // 배송등록위한 오더아이템
    @GetMapping("/item/detail")
    public ResponseEntity<OrderItemDeilveryDTO> getOrderItemDetail(@RequestParam Long id) {
        log.info("컨트롤러");
        OrderItemDeilveryDTO itemDetails = adminOrderService.getOrderItemDetail(id);
        return ResponseEntity.ok(itemDetails);
    }

    // 배송등록 (택배사 정보 insert 및 배송상태 변경)
    @PostMapping("/register-delivery")
    public ResponseEntity<?> registerDelivery(@RequestBody DeliveryDTO deliveryDTO) {
        try {
            log.info("dtooo" + deliveryDTO.toString());
            deliveryService.registerDelivery(deliveryDTO);
            return ResponseEntity.ok("배송 정보가 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("배송 등록에 실패했습니다.");
        }
    }

    @GetMapping("/daily")
    public ResponseEntity<List<OrderDailyQueryDTO>> getOrderDaily() {
        return ResponseEntity.status(HttpStatus.OK).body(adminQueryRepository.findOrderDailyQueryLastFourDays());
    }

    @GetMapping("/ratio")
    public ResponseEntity<List<OrderItemSalesRatioQueryDTO>> getOrderItemSalesRatio() {
        return ResponseEntity.status(HttpStatus.OK).body(adminQueryRepository.findOrderItemSalesRatioQuery());
    }

    @GetMapping("/operating")
    public ResponseEntity<OperatingStatusQueryDTO> getOperatingStatus() {
        Optional<OperatingStatusQueryDTO> operatingStatusQueryOpt = adminQueryRepository.findOperatingStatusQuery();

        if(operatingStatusQueryOpt.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(operatingStatusQueryOpt.get());
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
