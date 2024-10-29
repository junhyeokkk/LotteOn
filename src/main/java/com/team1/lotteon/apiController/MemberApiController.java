package com.team1.lotteon.apiController;

import com.team1.lotteon.dto.GeneralMemberDTO;
import com.team1.lotteon.service.admin.AdminMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/member")
@RequiredArgsConstructor
public class MemberApiController {

    private final AdminMemberService adminMemberService;

    // 모든 회원 목록 조회
    @GetMapping
    public ResponseEntity<List<GeneralMemberDTO>> getAllMembers() {
        List<GeneralMemberDTO> members = adminMemberService.getAllMembers();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    // 회원 정보 조회
    @GetMapping("/{uid}")
    public ResponseEntity<GeneralMemberDTO> getMemberById(@PathVariable String uid) {
        GeneralMemberDTO member = adminMemberService.getMemberByUid(uid);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    // 회원 등록
    @PostMapping
    public ResponseEntity<GeneralMemberDTO> createMember(@RequestBody GeneralMemberDTO memberDTO) {
        GeneralMemberDTO createdMember = adminMemberService.createMember(memberDTO);
        return new ResponseEntity<>(createdMember, HttpStatus.CREATED);
    }

    // 회원 정보 수정
    @PutMapping("/{uid}")
    public ResponseEntity<GeneralMemberDTO> updateMember(@PathVariable String uid, @RequestBody GeneralMemberDTO memberDTO) {
        GeneralMemberDTO updatedMember = adminMemberService.updateMember(uid, memberDTO);
        return new ResponseEntity<>(updatedMember, HttpStatus.OK);
    }

    // 회원 삭제
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteMember(@PathVariable String uid) {
        adminMemberService.deleteMember(uid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/update-grade")
    public ResponseEntity<Map<String, Object>> updateMemberGrade(@RequestBody List<GeneralMemberDTO> memberUpdates) {
        Map<String, Object> response = new HashMap<>();

        // 업데이트할 회원 정보가 비어있지 않은지 확인
        if (memberUpdates == null || memberUpdates.isEmpty()) {
            response.put("success", false);
            response.put("message", "업데이트할 회원 정보가 비어있습니다.");
            return ResponseEntity.badRequest().body(response); // 요청이 비어있다면 400 Bad Request 응답
        }

        try {
            for (GeneralMemberDTO updateRequest : memberUpdates) {
                // 회원 ID가 null인지 확인
                if (updateRequest.getUid() == null) {
                    throw new IllegalArgumentException("회원 id가 null 입니다."); // IllegalArgumentException 발생
                }

                // 회원 등급을 업데이트
                adminMemberService.updateMemberGrade(updateRequest.getUid(), updateRequest.getGrade());
            }

            response.put("success", true);
            response.put("message", "회원 등급이 성공적으로 수정되었습니다.");
            return ResponseEntity.ok(response); // 성공적으로 처리된 경우 200 OK와 JSON 응답 반환

        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response); // 유효하지 않은 요청일 경우 400 Bad Request와 JSON 응답 반환
        }
    }
}