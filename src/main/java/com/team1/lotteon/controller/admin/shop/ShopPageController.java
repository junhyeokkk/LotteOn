package com.team1.lotteon.controller.admin.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShopPageController {

    @GetMapping("/admin/shop/list")
    public String list(){

        return "admin/shop/list";
    }

    @GetMapping("/admin/shop/sales")
    public String sales(){

        return "admin/shop/sales";
    }
}
