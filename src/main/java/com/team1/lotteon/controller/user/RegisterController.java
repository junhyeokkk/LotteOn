package com.team1.lotteon.controller.user;

import com.team1.lotteon.dto.GeneralMemberDTO;
import com.team1.lotteon.dto.MemberDTO;
import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.dto.SellerMemberDTO;
import com.team1.lotteon.dto.ShopDTO;
import com.team1.lotteon.service.MemberService.GeneralMemberService;
import com.team1.lotteon.service.MemberService.MemberService;
import com.team1.lotteon.service.PointService;
import com.team1.lotteon.service.MemberService.SellerMemberService;
import com.team1.lotteon.service.ShopService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final MemberService memberService;
    private final GeneralMemberService generalMemberService;
    private final PointService pointService;
    private final ModelMapper modelMapper;
    private final SellerMemberService sellerMemberService;
    private final ShopService shopService;
    private final PasswordEncoder passwordEncoder;

    //회원가입 정보 입력 (판매자, 일반회원) 구분
    @GetMapping("/user/register/{member}")
    public String registerPage(@PathVariable String member, Model model) {
        log.info(member);
        if(member.equals("user")){
            return "user/register";
        }
        else if(member.equals("seller")){
            return "user/registerSeller";
        }
        return "user/login";
    }

    //입력받은 정보를 기반으로 DB 저장
    @PostMapping("/user/register/{role}")
    public String UserRegister(@PathVariable("role") String roleType,
                               GeneralMemberDTO generalMemberDTO, MemberDTO memberDTO, ShopDTO shopDTO,
                               HttpServletRequest request) {
        HttpSession session = request.getSession();

        try {
            if ("user".equals(roleType)) {
                // 일반 사용자 회원가입 처리
                if (memberService.insertGeneralMember(generalMemberDTO, memberDTO) != null){

                    GeneralMember savedMember = memberService.insertGeneralMember(generalMemberDTO, memberDTO);
                    GeneralMemberDTO savedMemberDTO = modelMapper.map(savedMember, GeneralMemberDTO.class);
                    // 회원가입 축하 기념 포인트 지급
                    int points = 1000; // 지급할 포인트 수량
                    String pointType = "회원가입 축하 포인트"; // 포인트 타입
                    pointService.registerPoint(savedMemberDTO, points, pointType);
                }
                return "redirect:/user/login?message=" + URLEncoder.encode("회원가입이 성공적으로 완료되었습니다. 회원가입 축하 기념 포인트가 지급되었습니다!", "UTF-8");

            } else if ("seller".equals(roleType)) {
                // 판매자 회원가입 처리
                memberService.insertSellerMember(shopDTO, memberDTO);
                return "redirect:/user/login?message=" + URLEncoder.encode("판매자 회원가입이 성공적으로 완료되었습니다.", "UTF-8");

            } else {
                // 잘못된 role 값 처리
                return "redirect:/error?error=" + URLEncoder.encode("잘못된 요청입니다.", "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            // 인코딩 예외 처리
            e.printStackTrace();
            // 인코딩 예외 발생 시 고정된 오류 메시지 반환
            return "redirect:/error?error=encoding_error";
        }
    }
    @PostMapping("/user/register/admin/{role}")
    public String AdminRegister(@PathVariable("role") String roleType,
                               GeneralMemberDTO generalMemberDTO, MemberDTO memberDTO, ShopDTO shopDTO,
                               HttpServletRequest request) {
        HttpSession session = request.getSession();

        try {
            if ("user".equals(roleType)) {
                // 일반 사용자 회원가입 처리
                if (memberService.insertGeneralMember(generalMemberDTO, memberDTO) != null){

                    GeneralMember savedMember = memberService.insertGeneralMember(generalMemberDTO, memberDTO);
                    GeneralMemberDTO savedMemberDTO = modelMapper.map(savedMember, GeneralMemberDTO.class);
                    // 회원가입 축하 기념 포인트 지급
                    int points = 1000; // 지급할 포인트 수량
                    String pointType = "회원가입 축하 포인트"; // 포인트 타입
                    pointService.registerPoint(savedMemberDTO, points, pointType);
                }
                return "redirect:/admin/member/list?message=" + URLEncoder.encode("회원가입이 성공적으로 완료되었습니다. 회원가입 축하 기념 포인트가 지급되었습니다!", "UTF-8");

            } else if ("seller".equals(roleType)) {
                // 판매자 회원가입 처리
                memberService.insertSellerMember(shopDTO, memberDTO);
                return "redirect:/admin/shop/list?message=" + URLEncoder.encode("판매자 회원가입이 성공적으로 완료되었습니다.", "UTF-8");

            } else {
                // 잘못된 role 값 처리
                return "redirect:/error?error=" + URLEncoder.encode("잘못된 요청입니다.", "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            // 인코딩 예외 처리
            e.printStackTrace();
            // 인코딩 예외 발생 시 고정된 오류 메시지 반환
            return "redirect:/error?error=encoding_error";
        }
    }
    //중복확인
    @GetMapping("/user/Register/{type}/{value}")
    @ResponseBody
    public Map<String, Boolean> checkUserRegister(HttpSession session, @PathVariable String type, @PathVariable String value) {
        Map<String, Boolean> response = new HashMap<>();

        log.info("Type: " + type + ", Value: " + value);

        if (type.equals("uid")) {
            boolean exists = memberService.isUidExist(value);
            response.put("result", exists);
            return response;
        }

        if (type.equals("email")) {
            boolean exists = generalMemberService.isEmailExist(value);
            if (!exists) {
                // 이메일 중복이 없으면 이메일 코드 전송
                memberService.sendEmailCode(session, value);
            }
            response.put("result", exists);
            return response;
        }
        //2024/11/04 이도영 아이디 비밀번호 찾기 기능
        if (type.equals("sendemail")) {
            boolean exists = generalMemberService.isEmailExist(value);
            if (exists) {
                // 이메일이 있으면 이메일 코드 전송
                memberService.sendEmailCode(session, value);
            }
            response.put("result", exists);
            return response;
        }

        if (type.equals("ph")) {
            boolean exists = generalMemberService.isphExist(value);
            response.put("result", exists);
            return response;
        }
        if(type.equals("shop")){
            boolean exists = shopService.isshopnameExist(value);
            response.put("result", exists);
            return response;
        }
        if(type.equals("businessregistration")){
            boolean exists = shopService.isBusinessRegistrationExist(value);
            log.info(exists);
            response.put("result", exists);
            return response;
        }
//        if(type.equals("e_commerce_registration")){
//            boolean exists = shopService.isECommerceRegistrationExist(value);
//            log.info(exists);
//            response.put("result", exists);
//            return response;
//        }
        response.put("result", false); // 기본값
        return response;
    }
    // 이메일 인증 코드 검사
    @ResponseBody
    @PostMapping("/user/Register/email")
    public ResponseEntity<?> checkEmail(HttpSession session, @RequestBody Map<String, String> jsonData){

        log.info("checkEmail code : " + jsonData);

        String receiveCode = jsonData.get("code");
        log.info("checkEmail receiveCode : " + receiveCode);

        String sessionCode = (String) session.getAttribute("code");

        if(sessionCode.equals(receiveCode)){
            // Json 생성
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", true);

            return ResponseEntity.ok().body(resultMap);
        }else{
            // Json 생성
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("result", false);

            return ResponseEntity.ok().body(resultMap);
        }
    }
}
