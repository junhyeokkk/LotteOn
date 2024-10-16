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

    @GetMapping("/admin/layout/{group}/{cate}")
    public String index(@PathVariable String group, @PathVariable String cate, Model model){

        log.info("컨트롤러 들어오니?");
        log.info("ggggggggg" + group);
        log.info(cate);
        model.addAttribute("group", group);
        model.addAttribute("cate", cate);

        return "admin/layout/admin_layout";
    }

    // admin_css는 3차분류까지 있기때문에 따로 컨트롤러 생성
    @GetMapping("/admin/layout/{group}/{cate1}/{cate2}")
    public String admin_cs_index(@PathVariable String group, @PathVariable String cate1, @PathVariable String cate2, Model model){

        log.info("컨트롤러 들어오니?");
        log.info("ggggggggg" + group);
        log.info(cate1);
        log.info(cate2);
        model.addAttribute("group", group);
        model.addAttribute("cate", cate1);
        model.addAttribute("cate", cate2);

        return "admin/layout/admin_layout";
    }

}
