package com.team1.lotteon.controller.admin.cs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminCsPageController {

    // notice
    @GetMapping("/admin/cs/notice/list")
    public String notice_list(){

        return "/admin/cs/notice/list";
    }

    @GetMapping("/admin/cs/notice/view")
    public String notice_view(){

        return "/admin/cs/notice/view";
    }

    @GetMapping("/admin/cs/notice/write")
    public String notice_write(){

        return "/admin/cs/notice/write";
    }

    @GetMapping("/admin/cs/notice/modify")
    public String notice_modify(){

        return "/admin/cs/notice/modify";
    }

    // faq
    @GetMapping("/admin/cs/faq/list")
    public String faq_list(){

        return "/admin/cs/faq/list";
    }

    @GetMapping("/admin/cs/faq/view")
    public String faq_view(){

        return "/admin/cs/faq/view";
    }

    @GetMapping("/admin/cs/faq/write")
    public String faq_write(){

        return "/admin/cs/faq/write";
    }

    @GetMapping("/admin/cs/faq/modify")
    public String faq_modify(){

        return "/admin/cs/faq/modify";
    }

    //qna
    @GetMapping("/admin/cs/qna/list")
    public String qna_list(){

        return "/admin/cs/qna/list";
    }

    @GetMapping("/admin/cs/qna/view")
    public String qna_view(){

        return "/admin/cs/qna/view";
    }

    @GetMapping("/admin/cs/qna/reply")
    public String qna_reply(){

        return "/admin/cs/qna/reply";
    }

}
