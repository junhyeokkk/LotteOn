package com.team1.lotteon.controller.my;

import com.team1.lotteon.entity.Address;
import com.team1.lotteon.security.MyUserDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Log4j2
@Controller
public class MyinfoController {
    @GetMapping("/myPage/home")
    public String home() {
        return "myPage/home";
    }

//    @GetMapping("/myPage/{cate}")
//    public String myinfoindex(@PathVariable String cate, Model model){
//
//        log.info("컨트롤러 들어오니?");
//        log.info(cate);
//        model.addAttribute("cate", cate);
//
//
//        return "myPage/layout/mypage_layout";
//    }
    @GetMapping("/myPage/info")
    public String myinfo(Model model){
        MyUserDetails userDetails = (MyUserDetails) model.getAttribute("userDetails");
        String birth = userDetails.getGeneralMember().getBirth().toString();
        String Email = userDetails.getGeneralMember().getEmail().toString();
        String phonenumber = userDetails.getGeneralMember().getPh();
        Address address = userDetails.getGeneralMember().getAddress();

        model.addAttribute("birth", birth);
        model.addAttribute("Email", Email);
        model.addAttribute("phonenumber", phonenumber);
        model.addAttribute("address", address);
        return "myPage/content/info";
    }
    @GetMapping("/myPage/coupon")
    public String mycoupon(Model model){
        return "myPage/content/coupon";
    }
    @GetMapping("/myPage/ordered")
    public String myordered(Model model){
        return "myPage/content/ordered";
    }
    @GetMapping("/myPage/point")
    public String mypoint(Model model){
        return "myPage/content/point";
    }
    @GetMapping("/myPage/qna")
    public String myqna(Model model){
        return "myPage/content/qna";
    }
    @GetMapping("/myPage/review")
    public String myreview(Model model){
        return "myPage/content/review";
    }
}
