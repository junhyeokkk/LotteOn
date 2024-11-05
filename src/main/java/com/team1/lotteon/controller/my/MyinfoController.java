package com.team1.lotteon.controller.my;

import com.team1.lotteon.dto.CouponTakeDTO;
import com.team1.lotteon.dto.order.OrderDTO;
import com.team1.lotteon.dto.order.OrderItemDTO;
import com.team1.lotteon.entity.Address;
import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.OrderItem;
import com.team1.lotteon.security.MyUserDetails;
import com.team1.lotteon.service.Order.OrderService;
import com.team1.lotteon.service.PointService;
import com.team1.lotteon.service.admin.CouponTakeService;
import com.team1.lotteon.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/*
     날짜 : 2024/10/21
     이름 : 이도영(최초 작성자)
     내용 : MyinfoController 생성

     수정이력
        - 2024-10-31 이도영 getPagedCouponsByMemberId 추가
*/
@Log4j2
@Controller
@RequiredArgsConstructor
public class MyinfoController {

    private final PointService pointService;
    private final DateUtil dateUtil;
    private final CouponTakeService couponTakeService;
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @GetMapping("/myPage/home")
    public String home(@AuthenticationPrincipal MyUserDetails myUserDetails, Model model) {
        GeneralMember member = myUserDetails.getGeneralMember();
        if(member == null){
            throw new IllegalArgumentException("로그인이 필요합니다!");
        }

        List<OrderItem> orderitems = orderService.getMyOrder(member.getUid());

        OrderItem orderItem = orderitems.get(0);
        OrderItemDTO OrderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);

        model.addAttribute("myorder", OrderItemDTO);

        return "myPage/home";
    }

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
        return "myPage/info";
    }
    
    //멤버 아이디를 가지고 와서 쿠폰 정보 출력
    @GetMapping("/myPage/coupon/{memberid}")
    public String getPagedCouponsByMemberId(
            @PathVariable String memberid, Pageable pageable, Model model) {
        Page<CouponTakeDTO> coupons = couponTakeService.findPagedCouponsByMemberId(memberid, pageable);
        // Model에 페이징된 데이터 추가
        model.addAttribute("coupons", coupons);
        // 뷰 리턴
        return "myPage/coupon";
    }

    @GetMapping("/myPage/ordered")

    public String myordered(Model model){
        return "myPage/ordered";
    }
    @GetMapping("/myPage/point")
    public String mypoint(){

        return "myPage/point";
    }


    @GetMapping("/myPage/qna")
    public String myqna(Model model){
        return "myPage/qna";
    }
    @GetMapping("/myPage/review")
    public String myreview(Model model){
        return "myPage/review";
    }
}
