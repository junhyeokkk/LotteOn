package com.team1.lotteon.controller.user;

import com.team1.lotteon.dto.GeneralMemberDTO;
import com.team1.lotteon.dto.MemberDTO;
import com.team1.lotteon.service.MemberService.GeneralMemberService;
import com.team1.lotteon.service.MemberService.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final MemberService memberService;
    private final GeneralMemberService generalMemberService;
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
    @PostMapping("/user/register/{role}")
    public String UserRegister(@PathVariable("role") String roleType, GeneralMemberDTO generalMemberDTO, MemberDTO memberDTO, RedirectAttributes redirectAttributes) {

        if ("user".equals(roleType)) {
            // 일반 사용자 회원가입 처리
            memberService.insertGeneralMember(generalMemberDTO, memberDTO);
            redirectAttributes.addFlashAttribute("successMessage", "회원가입이 성공적으로 완료되었습니다.");

        } else if ("seller".equals(roleType)) {
            // 판매자 회원가입 처리
//            memberService.insertSellerMember(generalMemberDTO, memberDTO);
//            redirectAttributes.addFlashAttribute("successMessage", "판매자 회원가입이 성공적으로 완료되었습니다.");

        } else {
            // 잘못된 role 값 처리
            redirectAttributes.addFlashAttribute("errorMessage", "잘못된 요청입니다.");
            return "redirect:/error";
        }

        return "redirect:/user/login";
    }
    // 아이디 중복 확인
    @GetMapping("/user/Register/{type}/{value}")
    @ResponseBody
    public boolean checkUserRegister(HttpSession session, @PathVariable String type, @PathVariable String value) {
        log.info("Type: " + type + ", Value: " + value);

        if(type.equals("uid")){
            return memberService.isUidExist(value);
        }
        if(type.equals("email")){
            boolean count = generalMemberService.isEmailExist(value);
            if(!count){
                memberService.sendEmailCode(session, value);
            }
            return count;
        }
        if(type.equals("ph")){
            return generalMemberService.isphExist(value);
        }
        return false;
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
