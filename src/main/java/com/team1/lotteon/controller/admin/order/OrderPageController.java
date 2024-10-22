package com.team1.lotteon.controller.admin.order;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderPageController {

    @GetMapping("/admin/order/delivery")
    public String delivery(){

        return "admin/order/delivery";
    }

    @GetMapping("/admin/order/list")
    public String list(){

        return "admin/order/list";
    }
}
