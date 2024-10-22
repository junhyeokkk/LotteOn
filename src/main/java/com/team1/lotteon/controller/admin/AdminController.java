package com.team1.lotteon.controller.admin;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Log4j2
@Controller
public class AdminController {



    @GetMapping(value = {"/admin","/admin/index"})
    public String index(){

        return "admin/index";
    }

}
