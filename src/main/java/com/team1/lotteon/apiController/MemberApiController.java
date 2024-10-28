package com.team1.lotteon.apiController;

import com.team1.lotteon.dto.GeneralMemberDTO;
import com.team1.lotteon.service.admin.AdminMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
