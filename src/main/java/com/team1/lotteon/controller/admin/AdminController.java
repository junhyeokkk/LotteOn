package com.team1.lotteon.controller.admin;

import com.team1.lotteon.dto.ConfigDTO;
import com.team1.lotteon.repository.query.OrderQueryRepository;
import com.team1.lotteon.repository.query.dto.OrderDailyQueryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping(value = "/admin")
    public String info(Model model){
        List<OrderDailyQueryDTO> orderDailyQueryDtoList = orderQueryRepository.findOrderDailyQueryLastFourDays();
        model.addAttribute("orderDailyQueryDtoList", orderDailyQueryDtoList);

        return "admin/index";
    }
}
