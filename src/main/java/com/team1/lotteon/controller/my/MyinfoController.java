package com.team1.lotteon.controller.my;

import com.team1.lotteon.dto.CouponTakeDTO;
import com.team1.lotteon.dto.order.OrderDTO;
import com.team1.lotteon.dto.order.OrderItemDTO;
import com.team1.lotteon.dto.point.PointPageRequestDTO;
import com.team1.lotteon.dto.point.PointPageResponseDTO;
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
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/myPage")

public class MyinfoController {

    private final PointService pointService;
    private final DateUtil dateUtil;
    private final CouponTakeService couponTakeService;
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @GetMapping("/home")
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

    @GetMapping("/info")
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
    @GetMapping("/coupon/{memberid}")
    public String getPagedCouponsByMemberId(
            @PathVariable String memberid, Pageable pageable, Model model) {
        Page<CouponTakeDTO> coupons = couponTakeService.findPagedCouponsByMemberId(memberid, pageable);
        // Model에 페이징된 데이터 추가
        model.addAttribute("coupons", coupons);
        // 뷰 리턴
        return "myPage/coupon";
    }

    @GetMapping("/ordered")
    public String myordered(Model model){
        return "myPage/ordered";
    }


    // 모든 마이페이지 요청에 대해 totalAcPoints를 모델에 추가
    @ModelAttribute("totalAcPoints")
    public Integer populateTotalAcPoints(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        return pointService.calculateTotalAcPoints(myUserDetails.getGeneralMember().getUid());
    }


    @GetMapping("/point")
    public String mypoint(    @RequestParam(defaultValue = "1") int pg,
                              @RequestParam(required = false) String type,
                              @RequestParam(required = false) String keyword,
                              @AuthenticationPrincipal MyUserDetails myUserDetails, // 로그인된 사용자 정보 가져오기
                              Model model) {

        // DTO 생성
        PointPageRequestDTO requestDTO = PointPageRequestDTO.builder()
                .pg(pg)
                .size(10)
                .type(type) // 타입 추가
                .keyword(keyword) // 키워드 추가
                .build();


        // 포인트 데이터 가져오기
        PointPageResponseDTO responseDTO = pointService.getPoints(requestDTO);
        model.addAttribute("points", responseDTO);

        // 포인트 합계 계산 후 Model에 추가
        Integer totalAcPoints = pointService.calculateTotalAcPoints(myUserDetails.getGeneralMember().getUid());
        model.addAttribute("totalAcPoints", totalAcPoints);


        // 포인트 리스트에 포맷팅된 생성일 및 유효기간 추가
        responseDTO.getDtoList().forEach(point -> {
            point.setFormattedCreatedAt(dateUtil.formatLocalDateTime(point.getCreatedat()));
            point.setFormattedExpirationDate(dateUtil.formatLocalDateTime(point.getExpirationDate()));
        });


        // 로그: DTO 리스트 출력
        log.info("포인트 데이터: " + responseDTO.getDtoList());

        return "myPage/point";
    }





    @GetMapping("/qna")
    public String myqna(Model model){
        return "myPage/qna";
    }





    @GetMapping("/review")
    public String myreview(Model model){
        return "myPage/review";
    }
}
