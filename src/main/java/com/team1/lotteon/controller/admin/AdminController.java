package com.team1.lotteon.controller.admin;

import com.team1.lotteon.repository.query.AdminQueryRepository;
import com.team1.lotteon.repository.query.dto.OperatingStatusQueryDTO;
import com.team1.lotteon.repository.query.dto.OrderDailyQueryDTO;
import com.team1.lotteon.repository.query.dto.OrderItemSalesRatioQueryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminQueryRepository adminQueryRepository;

    @GetMapping(value = "/admin")
    public String info(Model model){
        List<OrderDailyQueryDTO> orderDailyQueryDtoList = adminQueryRepository.findOrderDailyQueryLastFourDays();
        List<OrderItemSalesRatioQueryDTO> orderItemSalesRatioQueryDTOList = adminQueryRepository.findOrderItemSalesRatioQuery();
        OperatingStatusQueryDTO operatingStatusQueryDTO = adminQueryRepository.findOperatingStatusQuery().orElse(new OperatingStatusQueryDTO());

        model.addAttribute("orderDailyQueryDtoList", orderDailyQueryDtoList);
        model.addAttribute("orderItemSalesRatioQueryDtoList", orderItemSalesRatioQueryDTOList);
        model.addAttribute("operatingStatusQueryDTO", operatingStatusQueryDTO);
        return "admin/index";
    }
}
