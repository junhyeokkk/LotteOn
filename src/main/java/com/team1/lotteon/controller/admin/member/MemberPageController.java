package com.team1.lotteon.controller.admin.member;

import com.team1.lotteon.dto.Member.MemberPageRequestDTO;
import com.team1.lotteon.dto.Member.MemberPageResponseDTO;
import com.team1.lotteon.dto.PointDTO;
import com.team1.lotteon.dto.point.PointPageRequestDTO;
import com.team1.lotteon.dto.point.PointPageResponseDTO;
import com.team1.lotteon.service.MemberService.MemberService;
import com.team1.lotteon.service.PointService;
import com.team1.lotteon.service.admin.AdminMemberService;
import com.team1.lotteon.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.*;
/*
     날짜 : 2024/10/23
     이름 : 최준혁
     내용 : 관리자 유저 컨트롤러 생성
*/
@Log4j2
@RequiredArgsConstructor
@Controller
public class MemberPageController {
    private final AdminMemberService adminMemberService;
    private final PointService pointService;
    private final DateUtil dateUtil;

    @GetMapping("/admin/member/list")
    public String list(@ModelAttribute MemberPageRequestDTO pageRequestDTO, Model model) {
        // 서비스에서 페이징된 회원 목록을 가져옴
        MemberPageResponseDTO responseDTO = adminMemberService.getPagedMembers(pageRequestDTO);


        // 디버깅: 각 회원의 ID와 grade 값을 로그로 출력하여 확인
        responseDTO.getDtoList().forEach(member ->
                log.info("Member ID: " + member.getUid() + ", Grade: " + member.getGrade())
        );

        // 모델에 회원 목록과 페이지 정보 추가
        model.addAttribute("memberList", responseDTO.getDtoList());
        model.addAttribute("pageInfo", responseDTO);
        log.info("memberList: " + responseDTO.getDtoList());

        return "admin/member/list";


    }

    @GetMapping("/admin/member/point")
    public String point(
            @RequestParam(defaultValue = "1") int pg,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            Model model) {

        // 로그 추가: 타입과 키워드 출력
        log.info("타입: " + type);
        log.info("키워드: " + keyword);

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

        // 포맷팅된 날짜 추가
        responseDTO.getDtoList().forEach(point -> {
            point.setFormattedCreatedAt(dateUtil.formatLocalDateTime(point.getCreatedat()));
        });

        // 로그: DTO 리스트 출력
        log.info("포인트 데이터: " + responseDTO.getDtoList());

        return "admin/member/point";
    }

}
