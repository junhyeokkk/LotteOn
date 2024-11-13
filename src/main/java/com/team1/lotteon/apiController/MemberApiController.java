package com.team1.lotteon.apiController;

import com.team1.lotteon.dto.GeneralMemberDTO;
import com.team1.lotteon.service.MemberService.MemberService;
import com.team1.lotteon.service.admin.AdminMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
     날짜 : 2024/10/25
     이름 : 박서홍
     내용 : 관리자 유저 API 컨트롤러 생성
*/

@Slf4j
@RestController
@RequestMapping("/api/admin/member")
@RequiredArgsConstructor
public class MemberApiController {

    private final AdminMemberService adminMemberService;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    public static final int STATUS_ACTIVE = 0;     // 정상 상태
    public static final int STATUS_SUSPENDED = 2;   // 중지 상태
    public static final int STATUS_INACTIVE = 3;     // 휴면 상태
    public static final int STATUS_DEACTIVATED = 4;  // 비활성 상태 (탈퇴)
    public static final int STATUS_ADMIN = 5;  // 관리자 상태


    // 모든 회원 목록 조회
    @GetMapping("/list")
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
        log.info("memberrDTO" + memberDTO);
        GeneralMemberDTO updatedMember = adminMemberService.updateMember(uid, memberDTO);
        return new ResponseEntity<>(updatedMember, HttpStatus.OK);
    }

    //비밀번호 수정
    @PostMapping("/changepassword/{uid}")
    public ResponseEntity<String> changePassword(@PathVariable String uid, @RequestBody Map<String, String> payload) {
        // Extract newPassword from the payload
        String newPassword = payload.get("newPassword");
        String encodedPassword = passwordEncoder.encode(newPassword);
        // Call the service to update the password
        log.info(uid);
        log.info("newPasswordnewPassword" + newPassword);
        boolean isUpdated = memberService.updatePassword(uid, encodedPassword);
        if (isUpdated) {
            return ResponseEntity.ok("비밀번호가 성공적으로 변경 되었습니다");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 변경에 실패 했습니다.");
        }
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

    @PutMapping("/suspend/{uid}")
    public ResponseEntity<Map<String, Object>> suspendMember(@PathVariable String uid) {
        try {
            adminMemberService.updateMemberStatus(uid, STATUS_SUSPENDED);

            // JSON 형식의 응답 생성
            Map<String, Object> response = new HashMap<>();
            response.put("message", "회원 상태가 중지되었습니다.");
            response.put("newStatus", STATUS_SUSPENDED); // 새로운 상태 값을 포함

            return ResponseEntity.ok(response); // JSON 응답 반환
        } catch (Exception e) {
            // 예외 처리
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "회원 상태 변경 중 오류가 발생했습니다.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // 회원 상태 변경: 재개
    @PutMapping("/reactivate/{uid}")
    public ResponseEntity<Map<String, Object>> reactivateMember(@PathVariable String uid) {
        try {
            adminMemberService.updateMemberStatus(uid, STATUS_ACTIVE);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "회원 상태가 재개되었습니다."); // 메시지 추가
            response.put("newStatus", STATUS_ACTIVE); // 새로운 상태 값 추가

            return ResponseEntity.ok(response); // JSON 응답 반환
        } catch (Exception e) {
            // 오류 발생 시 JSON 형식으로 오류 메시지 반환
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "회원 상태 변경 중 오류가 발생했습니다.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    // 회원 상태 변경: 휴면으로 전환
    @PutMapping("/set-dormant/{uid}")
    public ResponseEntity<Map<String, Object>> setDormantMember(@PathVariable String uid) {
        try {
            adminMemberService.updateMemberStatus(uid, STATUS_INACTIVE); // STATUS_INACTIVE를 휴면 상태로 사용

            Map<String, Object> response = new HashMap<>();
            response.put("message", "회원 상태가 휴면으로 전환되었습니다.");
            response.put("newStatus", STATUS_INACTIVE); // 새로운 상태 값 추가

            return ResponseEntity.ok(response); // JSON 응답 반환
        } catch (Exception e) {
            // 오류 발생 시 JSON 형식으로 오류 메시지 반환
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "회원 상태 변경 중 오류가 발생했습니다.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // 회원 상태 변경: 비활성 (탈퇴)
    @DeleteMapping("/deactivate/{uid}")
    public ResponseEntity<Map<String, Object>> deactivateMember(@PathVariable String uid) {
        try {
            // 회원 상태를 탈퇴로 변경
            adminMemberService.updateMemberStatus(uid, STATUS_DEACTIVATED);

            // 회원 정보를 DB에서 삭제
            adminMemberService.deleteMemberInfo(uid); // 삭제 메서드 호출

            Map<String, Object> response = new HashMap<>();
            response.put("message", "회원 상태가 비활성되었습니다."); // 메시지 추가
            response.put("newStatus", STATUS_DEACTIVATED); // 새로운 상태 값 추가
            return ResponseEntity.ok(response); // JSON 응답 반환
        } catch (Exception e) {
            // 오류 발생 시 JSON 형식으로 오류 메시지 반환
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "회원 상태 변경 중 오류가 발생했습니다.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/admin/member/update-status/{uid}")
    @ResponseBody
    public ResponseEntity<?> updateMemberStatus(@PathVariable String uid, @RequestBody Map<String, Integer> statusRequest) {
        int status = statusRequest.get("status");
        try {
            adminMemberService.updateMemberStatus(uid, status);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}