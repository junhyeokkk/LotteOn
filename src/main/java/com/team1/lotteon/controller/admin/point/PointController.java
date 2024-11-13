package com.team1.lotteon.controller.admin.point;

import com.team1.lotteon.dto.GeneralMemberDTO;
import com.team1.lotteon.dto.PointDTO;
import com.team1.lotteon.dto.point.PointPageRequestDTO;
import com.team1.lotteon.dto.point.PointPageResponseDTO;
import com.team1.lotteon.entity.GeneralMember;
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

import java.util.Arrays;
import java.util.List;

/*
 *    날짜 : 2024/11/02
 *    이름 : 박서홍
 *    내용 : 관리자 포인트 컨트롤러 생성
 *
 *    수정이력
 *  - 2025/11/04 박서홍 - 주문하기 - 포인트 사용 코드 생성
 *
*/
@Log4j2
@RequiredArgsConstructor
@Controller
public class PointController {
    private final PointService pointService;
    private final GeneralMemberRepository generalMemberRepository;
    private final ModelMapper getModelMapper;

    @PostMapping("/admin/member/give")
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

    @GetMapping("/my/points")
    public ResponseEntity<PointPageResponseDTO> getMyPoints(PointPageRequestDTO requestDTO) {
        PointPageResponseDTO responseDTO = pointService.getMyPoints(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }



    @PostMapping("/admin/member/delete")
    public ResponseEntity<String> deleteSelectedPoints(@RequestBody List<Long> pointIds) {
        log.info("deleteSelectedPoints 호출 - pointIds: {}", pointIds);
        if (pointIds == null || pointIds.isEmpty()) {
            return ResponseEntity.badRequest().body("삭제할 포인트 ID가 필요합니다.");
        }

        // 선택된 포인트 내역 삭제
        pointService.deleteSelectedPoints(pointIds);
        log.info("선택된 포인트가 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok("선택된 포인트가 성공적으로 삭제되었습니다.");
    }


}
