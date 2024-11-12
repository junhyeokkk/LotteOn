package com.team1.lotteon.controller.company;

import com.team1.lotteon.dto.RecruitDTO;
import com.team1.lotteon.service.admin.RecruitService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


/*
     날짜 : 2024/10/23
     이름 : 박서홍
     내용 : 회사소개 컨트롤러 생성
*/

@Log4j2
@Controller
public class PageController {

    @GetMapping("/company/index")
    public String index() {
        return "company/index";
    }

    @GetMapping("/company/{group}/{cate}")
    public String index(@PathVariable String group, @PathVariable String cate, Model model){

        log.info("Controller accessed");
        log.info("Group: " + group);
        log.info("Category: " + cate);

        String category = "content";
        if ("story".equalsIgnoreCase(cate) || "story".equalsIgnoreCase(group)) {
            category = "story";
        }

        model.addAttribute("group", group);
        model.addAttribute("cate", cate);
        model.addAttribute("category", category);
        return "company/layout/company_layout";
    }

}



