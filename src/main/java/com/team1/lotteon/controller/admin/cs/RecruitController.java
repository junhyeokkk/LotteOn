package com.team1.lotteon.controller.admin.cs;

import com.team1.lotteon.dto.MemberDTO;
import com.team1.lotteon.dto.pageDTO.NewPageRequestDTO;
import com.team1.lotteon.dto.pageDTO.NewPageResponseDTO;
import com.team1.lotteon.entity.Recruit;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import com.team1.lotteon.dto.RecruitDTO;
import com.team1.lotteon.service.admin.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    날짜 : 2024/11/01
    이름 : 강유정
    내용 : 채용 컨트롤러 생성
*/

@Log4j2
@Controller
@RequiredArgsConstructor
public class RecruitController {

    private final RecruitService recruitService;

    // 채용 목록 페이지로 이동 (검색 포함)
    @GetMapping("/admin/cs/recruit/list")
    public String getRecruitList(@RequestParam(required = false) String type,
                                 @RequestParam(required = false) String keyword,
                                 NewPageRequestDTO newPageRequestDTO, Model model) {
        // type, keyword가 null일 경우 기본값 설정

        newPageRequestDTO.setType(type);
        newPageRequestDTO.setKeyword(keyword);
        // 검색 및 페이지네이션 처리된 결과 받기
        NewPageResponseDTO<RecruitDTO> recruitResponse = recruitService.getRecruitsWithPagination(newPageRequestDTO);


        // 모델에 데이터 추가
        model.addAttribute("recruitResponses", recruitResponse);  // 현재 페이지 번호
        model.addAttribute("keyword", keyword);  // 검색 키워드 전달
        model.addAttribute("type", type);  // 검색 타입 전달
        model.addAttribute("currentPage", newPageRequestDTO.getPg());  // 현재 페이지 번호 전달
        return "admin/cs/recruit/list";  // view로 반환
    }

    // 채용 등록 모달에서 데이터 저장
    @PostMapping("/admin/cs/recruit/list")
    public String registerRecruit(@RequestBody RecruitDTO recruitDTO) {
        log.info("컨트롤러 입성: " + recruitDTO.toString());

        // RecruitDTO를 Recruit 엔티티로 매핑 후 저장
        recruitService.saveRecruitDetails(recruitDTO);

        // 성공 후 리다이렉트 (목록 페이지로 이동)
        return "redirect:/admin/cs/recruit/list"; // 등록 후 목록으로 리다이렉트
    }

    // 삭제
    @PostMapping("/admin/recruit/delete")
    @ResponseBody
    public Map<String, Object> deleteRecruits(@RequestBody Map<String, List<Long>> requestData) {
        List<Long> recruitIds = requestData.get("recruitIds");
        Map<String, Object> response = new HashMap<>();

        try {
            // Service 레이어를 통해 삭제 처리
            recruitService.deleteRecruitsByIds(recruitIds);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
        }

        return response;
    }


    // 회사소개
    @GetMapping("/company/content/recruit")
    public String showActiveRecruits(Model model) {
        List<RecruitDTO> activeRecruits = recruitService.getActiveRecruits();
        model.addAttribute("activeRecruits", activeRecruits);
        return "company/content/recruit";  // recruit.html 경로
    }

}
