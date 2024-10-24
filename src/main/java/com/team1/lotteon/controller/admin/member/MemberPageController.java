package com.team1.lotteon.controller.admin.member;

import com.team1.lotteon.dto.Member.MemberPageRequestDTO;
import com.team1.lotteon.dto.Member.MemberPageResponseDTO;
import com.team1.lotteon.service.admin.AdminMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
@Log4j2
@RequiredArgsConstructor
@Controller
public class MemberPageController {

    private final AdminMemberService adminMemberService;


    @GetMapping("/admin/member/list")
    public String list(@ModelAttribute MemberPageRequestDTO pageRequestDTO, Model model) {
        // 서비스에서 페이징된 회원 목록을 가져옴
        MemberPageResponseDTO responseDTO = adminMemberService.getPagedMembers(pageRequestDTO);

        // 모델에 회원 목록과 페이지 정보 추가
        model.addAttribute("memberList", responseDTO.getDtoList());
        model.addAttribute("pageInfo", responseDTO);
        log.info("memberList: " + responseDTO.getDtoList());


        return "admin/member/list"; // 뷰 이름 반환
    }


    @GetMapping("/admin/member/point")
    public String point(){

        return "admin/member/point";
    }
}
