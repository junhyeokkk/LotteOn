package com.team1.lotteon.controller.admin;

import com.team1.lotteon.dto.ConfigDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping(value = "/admin")
    public String info(Model model){


        return "/admin/index";
    }
}
