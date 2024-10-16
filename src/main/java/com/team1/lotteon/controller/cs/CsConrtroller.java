package com.team1.lotteon.controller.cs;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Log4j2
@Controller
public class CsConrtroller {

    @GetMapping("/cs/index")
    public String index(){

        return "cs/index";
    }

    @GetMapping("/cs/layout/{group}/{cate}")
    public String index(@PathVariable String group, @PathVariable String cate, Model model){

        log.info("컨트롤러 들어오니?");
        log.info("ggggggggg" + group);
        log.info(cate);
        model.addAttribute("group", group);
        model.addAttribute("cate", cate);

        return "cs/layout/cs_layout";
    }



}
