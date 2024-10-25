package com.team1.lotteon.apiController.admin.config;

import com.team1.lotteon.dto.BannerDTO;
import com.team1.lotteon.service.admin.BannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/config")
public class BannerApiController {

    private final BannerService bannerService;

    // 배너 등록
    @PostMapping("/banner")
    public ResponseEntity<Map<String, Object>> uploadBanner(
            @RequestParam("banner_img") MultipartFile bannerImg,
            @ModelAttribute BannerDTO bannerDTO

    ) {
        Map<String, Object> response = new HashMap<>();

        log.info("afsdfaf" + bannerImg);

        try {
            log.info("Uploading banner imageasfddddddddddddddddddddddddddd " );
            // 이미지 저장 로직은 서비스에서 처리
            String imagePath = bannerService.saveBannerImage(bannerImg);
            bannerDTO.setImg(imagePath);

            // 배너 세부 정보 저장 (DB 저장 로직)
            bannerService.saveBannerDetails(bannerDTO);

            response.put("status", "success");
            response.put("message", "Banner uploaded successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "Error occurred during file upload");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (IllegalArgumentException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/banner")
    @ResponseBody // 이 어노테이션을 추가하여 JSON 응답을 반환하도록 설정
    public ResponseEntity<Map<String, Object>> banner() {  // 기본 페이지 크기는 5

        log.info("들어오긴하니?");

        // 서비스에서 배너 데이터를 받아옴
        List<BannerDTO> banners = bannerService.getAllBanners();

        log.info("afsdasfddddddddd"+banners);
        // 응답을 위한 Map 생성
        Map<String, Object> response = new HashMap<>();
        response.put("banners", banners);

        return ResponseEntity.ok(response); // JSON 형태로 반환
    }



}