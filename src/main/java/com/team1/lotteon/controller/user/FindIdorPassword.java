package com.team1.lotteon.controller.user;

import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.service.MemberService.GeneralMemberService;
import com.team1.lotteon.service.MemberService.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
/*
    날짜 : 2024/11/04
    이름 : 이도영
    내용 : 아이디 비밀번호 찾기
*/
@Log4j2
@Controller
@RequiredArgsConstructor
public class FindIdorPassword {
    private final GeneralMemberService generalMemberService;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    @GetMapping("/user/findId")
    public String findId(){
        return "user/findId";
    }
    @PostMapping("/user/findId/check")
    public String findId(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            Model model) {

        GeneralMember generalmember = generalMemberService.findUserIdByNameAndEmail(name, email);

        if (generalmember != null) {
            log.info("uid1111 : "+generalmember);
            model.addAttribute("generalmember", generalmember);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedCreatedAt = generalmember.getCreatedAt().format(formatter);
            model.addAttribute("formattedCreatedAt", formattedCreatedAt);
            return "user/findIdResult";
        } else {
            model.addAttribute("error", "해당 정보와 일치하는 계정이 없습니다.");
            return "user/findId";
        }
    }

    @GetMapping("/user/findPass")
    public String findPass(){
        return "user/findPass";
    }
    //비밀번호 찾기 결과
    @PostMapping("/user/findPass/check")
    public String findPass(
            @RequestParam("uid") String uid,
            @RequestParam("email") String email,
            Model model) {

        // uid와 email로 회원 정보 조회
        GeneralMember generalmember = generalMemberService.findByUidAndEmail(uid, email);

        if (generalmember != null) {
            model.addAttribute("generalmember", generalmember); // 회원 정보 모델에 추가
            return "user/findPassResult"; // 비밀번호 변경 페이지로 이동
        } else {
            model.addAttribute("error", "해당 정보와 일치하는 계정이 없습니다.");
            return "user/findPass"; // 계정 정보가 없을 경우, 다시 비밀번호 찾기 페이지로 이동
        }
    }
    //비밀번호 변경
    @PostMapping("/user/changePassword")
    public String changePassword(
            @RequestParam("uid") String uid,
            @RequestParam("pass") String pass,
            @RequestParam("pass2") String pass2,
            Model model) throws UnsupportedEncodingException {

        // 암호화된 비밀번호 설정
        String encodedPassword = passwordEncoder.encode(pass);
        boolean isUpdated = memberService.updatePassword(uid, encodedPassword);

        if (isUpdated) {
            model.addAttribute("success", "");
            return "redirect:/user/login?message=" + URLEncoder.encode("비밀번호가 성공적으로 변경되었습니다. 다시 로그인 해주세요", "UTF-8");
        } else {
            model.addAttribute("error", "비밀번호 변경에 실패했습니다.");
        }

        return "user/findPassResult"; // 결과 페이지로 이동
    }
}
