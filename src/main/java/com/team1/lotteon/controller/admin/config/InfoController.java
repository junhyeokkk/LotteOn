package com.team1.lotteon.controller.admin.config;

import com.team1.lotteon.service.admin.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/config")
public class InfoController {

    private final ConfigService configService;

    // 사이트 정보수정
    @PatchMapping("/updateSite")
    public ResponseEntity<?> patchSiteInfo(@RequestBody Map<String, String> payload) {
        try {
            String title = payload.get("title");
            String subTitle = payload.get("sub_title");

            configService.updateSiteInfo(title, subTitle);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false));
        }
    }

    // 회사 정보수정
    @PatchMapping("/updateCom")
    public ResponseEntity<?> patchComInfo(@RequestBody Map<String, String> payload) {
        try {
            String b_name = payload.get("b_name");
            String ceo = payload.get("ceo");
            String b_num = payload.get("b_num");
            String b_report = payload.get("b_report");
            String addr1 = payload.get("addr1");
            String addr2 = payload.get("addr2");

            configService.updateComInfo(b_name, ceo,b_num, b_report, addr1, addr2);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false));
        }
    }

    // 고객센터 정보수정
    @PatchMapping("/updateCs")
    public ResponseEntity<?> patchCsInfo(@RequestBody Map<String, String> payload) {
        try {
            String cs_num = payload.get("cs_num");
            String cs_time = payload.get("cs_time");
            String cs_email = payload.get("cs_email");
            String dispute = payload.get("dispute");

            configService.updateCsInfo(cs_num, cs_time, cs_email, dispute);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false));
        }
    }

    // 카피라이트 정보수정
    @PatchMapping("/updateCopy")
    public ResponseEntity<?> patchCopyInfo(@RequestBody Map<String, String> payload) {
        try {
            String copyright = payload.get("copyright");

            configService.updateCopyInfo(copyright);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false));
        }
    }

    @PostMapping("/uploadLogo")
    public ResponseEntity<?> uploadLogos(@RequestParam("file1") MultipartFile headerLogo,
                                         @RequestParam("file2") MultipartFile footerLogo,
                                         @RequestParam("file3") MultipartFile favicon) {
        try {

            // Service에서 파일 업로드 로직 호출
            configService.uploadLogos(headerLogo, footerLogo, favicon);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"success\": false}");
        }

    }
}
