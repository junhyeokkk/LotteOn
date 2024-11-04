package com.team1.lotteon.controller.admin.point;


import com.team1.lotteon.dto.GeneralMemberDTO;
import com.team1.lotteon.dto.PointDTO;
import com.team1.lotteon.dto.point.PointPageRequestDTO;
import com.team1.lotteon.dto.point.PointPageResponseDTO;
import com.team1.lotteon.repository.Memberrepository.GeneralMemberRepository;
import com.team1.lotteon.service.MemberService.MemberService;
import com.team1.lotteon.service.PointService;
import com.team1.lotteon.service.admin.AdminMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
     날짜 : 2024/11/02
     이름 : 박서홍
     내용 : 관리자 포인트 컨트롤러 생성
*/
@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/member")
public class PointController {
    private final PointService pointService;
    private final GeneralMemberRepository generalMemberRepository;
    private final ModelMapper getModelMapper;

    @PostMapping("/give")
    public ResponseEntity<String> givePoints(@RequestBody PointDTO pointDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자입니다.");
        }

        String uid = ((UserDetails) authentication.getPrincipal()).getUsername();
        GeneralMemberDTO generalMemberDTO = generalMemberRepository.findByUid(uid)
                .map(member -> getModelMapper.map(member, GeneralMemberDTO.class))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        pointService.registerPoint(generalMemberDTO, pointDTO.getGivePoints(), pointDTO.getType());

        return ResponseEntity.ok("포인트가 성공적으로 지급되었습니다.");
    }


    // 포인트 차감
    @PostMapping("/deduct")
    public ResponseEntity<String> deductPoints(@RequestBody PointDTO pointDTO) {
        pointService.deductPoints(pointDTO);
        return ResponseEntity.ok("포인트가 성공적으로 차감되었습니다.");
    }

    // 내 포인트 조회 (MYPAGE)
    @GetMapping("/my")
    public ResponseEntity<PointPageResponseDTO> getMyPoints(@RequestBody PointPageRequestDTO pointPageRequestDTO) {
        PointPageResponseDTO response = pointService.getMyPoints(pointPageRequestDTO);
        return ResponseEntity.ok(response);
    }

    // 포인트 목록 조회 (ADMIN)
    @GetMapping("/")
    public ResponseEntity<PointPageResponseDTO> getPoints(PointPageRequestDTO pointPageRequestDTO) {
        PointPageResponseDTO response = pointService.getPoints(pointPageRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteSelectedMembers(@RequestBody List<Long> memberIds) {
        log.info("deleteSelectedMembers 호출 - memberIds: {}", memberIds);
        if (memberIds == null || memberIds.isEmpty()) {
            return ResponseEntity.badRequest().body("삭제할 회원 ID가 필요합니다.");
        }

        // 선택된 회원 삭제 및 포인트 회수
        pointService.deleteSelectedMembers(memberIds);
        log.info("선택된 회원이 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok("선택된 회원이 성공적으로 삭제되었습니다.");
    }

}
