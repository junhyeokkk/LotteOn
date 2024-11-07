package com.team1.lotteon.controller.my;

import com.team1.lotteon.dto.CouponTakeDTO;
import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.order.OrderDTO;
import com.team1.lotteon.dto.order.OrderItemDTO;
import com.team1.lotteon.dto.order.OrderPageRequestDTO;
import com.team1.lotteon.dto.order.OrderPageResponseDTO;
import com.team1.lotteon.dto.point.PointPageRequestDTO;
import com.team1.lotteon.dto.point.PointPageResponseDTO;
import com.team1.lotteon.dto.review.ReviewResponseDTO;
import com.team1.lotteon.entity.Address;
import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.entity.OrderItem;
import com.team1.lotteon.security.MyUserDetails;
import com.team1.lotteon.service.MemberService.GeneralMemberService;
import com.team1.lotteon.service.MemberService.GeneralMemberService;
import com.team1.lotteon.service.Order.OrderService;
import com.team1.lotteon.service.PointService;
import com.team1.lotteon.service.admin.CouponTakeService;
import com.team1.lotteon.service.review.ReviewService;
import com.team1.lotteon.service.review.ReviewService;
import com.team1.lotteon.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
     날짜 : 2024/10/21
     이름 : 이도영(최초 작성자)
     내용 : MyinfoController 생성

     수정이력
        - 2024/10/31 이도영 getPagedCouponsByMemberId 추가
        - 2024/11/06 이도영 나의정보 전체 화면 보유쿠폰,구매상품 숫자 출력
                           나의정보 홈 화면에서 상품 최대 3개까지 출력 하도록 수정
                           나의정보 수정 화면에서 가지고 오는 데이터 방식 리펙토링
        - 2024/11/07 이상훈 리뷰 추가
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
    private final CouponTakeService coupontakeService;
    private final GeneralMemberService generalMemberService;
    private final ModelMapper modelMapper;
    private final ReviewService reviewService;

    @ModelAttribute("couponCount")
    public int getCouponCount(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        // 사용자 ID를 가져와 쿠폰 수량 조회
        String userId = myUserDetails.getGeneralMember().getUid();
        return coupontakeService.getCouponCountByUserId(userId);
    }
    @ModelAttribute("orderCount")
    public int getOrderCount(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        String userId = myUserDetails.getGeneralMember().getUid();
        return orderService.getOrderCountByUserId(userId);
    }
    // 모든 마이페이지 요청에 대해 totalAcPoints를 모델에 추가
    @ModelAttribute("totalAcPoints")
    public Integer populateTotalAcPoints(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        return pointService.calculateTotalAcPoints(myUserDetails.getGeneralMember().getUid());
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal MyUserDetails myUserDetails, Model model) {
        GeneralMember member = myUserDetails.getGeneralMember();
        if(member == null){
            throw new IllegalArgumentException("로그인이 필요합니다!");
        }
        //구매 정보
        List<OrderItem> orderitems = orderService.getMyOrder(member.getUid());

        // 최대 3개의 OrderItem만 가져오고, DTO로 변환
        List<OrderItemDTO> OrderItemDTO = orderitems.stream()
                .limit(5) // 최대 3개 제한
                .map(orderItem -> modelMapper.map(orderItem, OrderItemDTO.class)) // DTO로 매핑
                .collect(Collectors.toList());

        // 나의 정보
        Address address = member.getAddress();

        model.addAttribute("myorders", OrderItemDTO);
        model.addAttribute("member", member);
        model.addAttribute("address", address);
        return "myPage/home";
    }

    @GetMapping("/info")
    public String myinfo(@AuthenticationPrincipal MyUserDetails myUserDetails,Model model){
        Optional<GeneralMember> member = generalMemberService.findByUid(myUserDetails.getGeneralMember().getUid());

        String birth = String.valueOf(member.get().getBirth());
        String Email = member.get().getEmail();
        String phonenumber = member.get().getPh();
        Address address = member.get().getAddress();

        model.addAttribute("birth", birth);
        model.addAttribute("Email", Email);
        model.addAttribute("phonenumber", phonenumber);
        model.addAttribute("address", address);
        return "myPage/info";
    }
    @PostMapping("/info/delete/{uid}")
    public ResponseEntity<String> myinfoDelete(@PathVariable String uid) {
        generalMemberService.deactivateMember(uid);  // 서비스 계층에서 탈퇴 처리
        return ResponseEntity.ok("탈퇴가 성공적으로 처리되었습니다. 이용해 주셔서 감사합니다.");
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
    public String getOrderHistory(
            @RequestParam(defaultValue = "1") int pg,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            Model model) {

        String uid = myUserDetails.getGeneralMember().getUid();

        // OrderPageRequestDTO 객체 생성
        OrderPageRequestDTO orderRequestDTO = OrderPageRequestDTO.builder()
                .pg(pg)
                .size(10)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // 주문 내역 조회
        OrderPageResponseDTO orderResponseDTO = orderService.getOrdersByDateRange(uid, orderRequestDTO);
        model.addAttribute("orders", orderResponseDTO);

        return "myPage/ordered";
    }





    @GetMapping("/point")
    public String mypoint(@RequestParam(defaultValue = "1") int pg,
                          @RequestParam(required = false) String startDate, // 추가된 필드
                          @RequestParam(required = false) String endDate,   // 추가된 필드
                          @AuthenticationPrincipal MyUserDetails myUserDetails,
                          Model model) {

        // DTO 생성
        PointPageRequestDTO requestDTO = PointPageRequestDTO.builder()
                .pg(pg)
                .size(10)
                .startDate(startDate) // 추가
                .endDate(endDate)     // 추가
                .build();




        // 포인트 데이터 가져오기
        PointPageResponseDTO responseDTO = pointService.getMyPoints(requestDTO);
        model.addAttribute("points", responseDTO);

        // 포인트 합계 계산 후 Model에 추가
        Integer totalAcPoints = pointService.calculateTotalAcPoints(myUserDetails.getGeneralMember().getUid());
        model.addAttribute("totalAcPoints", totalAcPoints);

        // 포맷팅된 생성일 및 유효기간 추가
        responseDTO.getDtoList().forEach(point -> {
            point.setFormattedCreatedAt(dateUtil.formatLocalDateTime(point.getCreatedat()));
            point.setFormattedExpirationDate(dateUtil.formatLocalDateTime(point.getExpirationDate()));
        });

        log.info("포인트 데이터: " + responseDTO.getDtoList());
        return "myPage/point";
    }


    @GetMapping("/qna")
    public String myqna(Model model){
        return "myPage/qna";
    }

    @GetMapping("/review")
    public String myreview(Model model, @PageableDefault Pageable pageable, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        if(myUserDetails == null)
        {
            return "redirect:/user/login";
        }

        PageResponseDTO<ReviewResponseDTO> reviews = reviewService.getReviewsByUid(myUserDetails.getUsername(), pageable);
        model.addAttribute("reviews", reviews);

        int currentPage = reviews.getCurrentPage() + 1; // 타임리프는 1-based 인덱스 사용
        int totalPages = reviews.getTotalPages();

        int startPage = Math.max(1, currentPage - 2);
        int endPage = Math.min(currentPage + 2, totalPages);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "myPage/review";
    }
}
