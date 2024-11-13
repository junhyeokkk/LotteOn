package com.team1.lotteon.service.admin;

import com.team1.lotteon.dto.BannerDTO;
import com.team1.lotteon.entity.Banner;
import com.team1.lotteon.repository.BannerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*
    날짜 : 2024/10/22
    이름 : 최준혁
    내용 : 배너 서비스 생성

    수정내역
    - 배너 이미지 업로드 (10/22)
    - 배너 db 저장 (10/22)
    - 모든 배너 select (10/22)
    - 시간별 배너 동작 상태 변경 (11/12 이도영)
*/

@Log4j2
@RequiredArgsConstructor
@Service
public class BannerService {

    private final BannerRepository bannerRepository;
    private final ModelMapper modelMapper;


    @Value("${spring.servlet.multipart.location}")
    private String uploadDir; // YAML에서 설정한 파일 업로드 경로

    // 배너 이미지 업로드 (준혁)
    public String saveBannerImage(MultipartFile bannerImg) throws IOException {
        log.info("Attempting to save banner image...");

        // 파일 업로드 경로 파일 객체 생성
        File fileUploadPath = new File(uploadDir+"/banner");

        // 파일 업로드 시스템 경로 구하기
        String logouploadDir = fileUploadPath.getAbsolutePath();

        if (bannerImg == null || bannerImg.isEmpty()) {
            throw new IllegalArgumentException("Banner image is empty or null.");
        }

        // 파일을 저장할 디렉토리 생성
        if (!Files.exists(Paths.get(logouploadDir))) {
            Files.createDirectories(Paths.get(logouploadDir));
        }

        // 원본 파일 이름을 가져와서 고유한 이름 생성
        String originalFileName = bannerImg.getOriginalFilename();
        if (originalFileName == null) {
            throw new IllegalArgumentException("Original filename is null.");
        }

        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;
        File destinationFile = new File(logouploadDir, uniqueFileName);

        try {
            // 파일 저장
            bannerImg.transferTo(destinationFile);
            log.info("Banner image saved to: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Error saving banner image: " + e.getMessage(), e);
            throw new IOException("Failed to save banner image", e);
        }

        // 업로드된 파일 경로 반환
        return "/uploads/banner/" + uniqueFileName;
    }

    // 배너 db 저장 (준혁)
    @CacheEvict(value = "activeBanners", allEntries = true)
    public void saveBannerDetails(BannerDTO bannerDTO) {
        log.info("Sav image????????????????????");
        Banner banner = modelMapper.map(bannerDTO, Banner.class);
        bannerRepository.save(banner);
    }

    // 모든 배너 select (준혁)
    public List<BannerDTO> getAllBanners() {

        List<Banner> banners = bannerRepository.findAll();

        List<BannerDTO> bannerDTOList = banners.stream().map(banner -> modelMapper.map(banner, BannerDTO.class)).collect(Collectors.toList());
        return bannerDTOList;
    }

    // 현재 활성화된 배너 조회하여 캐싱
    @Cacheable(value = "activeBanners", key = "'bannerCache'")
    public List<Banner> getActiveBanners() {
        return bannerRepository.findActiveBannersByDateAndTime(LocalDate.now(), LocalTime.now());
    }

    // 매분마다 스케줄 실행 - 활성화/비활성화 시간을 기준으로 캐시 갱신
    @Scheduled(cron = "0 * * * * *") // 매 1분마다 실행
    @CacheEvict(value = "activeBanners", allEntries = true, condition = "@bannerService.isUpdateNeeded()")
    public void updateBannerCacheIfNeeded() {
        // 조건에 따라 캐시 갱신
    }

    // 활성화/비활성화 시간 확인 후 갱신이 필요한지 체크하는 메서드
    public boolean isUpdateNeeded() {
        List<Banner> activeBanners = bannerRepository.findActiveBannersByDateAndTime(LocalDate.now(), LocalTime.now());
        // 현재 캐시에 저장된 배너 목록과 DB에서 조회한 활성 배너 목록을 비교하여 차이가 있으면 true 반환
        return true; // 예시 - 실제로는 캐시와 DB 상태 비교 구현 필요
    }

    // 수정이나 삭제 이벤트 시 캐시 무효화
    @CacheEvict(value = "activeBanners", allEntries = true)
    public void updateBanner(Banner banner) {
        bannerRepository.save(banner); // 배너 저장 또는 수정
    }

    @CacheEvict(value = "activeBanners", allEntries = true)
    public void deleteBanner(Long bannerId) {
        bannerRepository.deleteById(Math.toIntExact(bannerId)); // 배너 삭제
    }

    public List<Banner> getBannersByPosition(String position) {
        // position이 지정된 배너 중 활성화된(isActive = 1) 배너만 조회
        return bannerRepository.findByPositionAndIsActive(position, 1);
    }

    // 배너의 활성 상태를 갱신 (캐시 무효화 추가)
    @Transactional
    @CacheEvict(value = "activeBanners", allEntries = true)
    public boolean updateBannerActiveState(Long bannerId, int newState) {
        return bannerRepository.findById(Math.toIntExact(bannerId)).map(banner -> {
            banner.setIsActive(newState); // 0 또는 1로 설정
            bannerRepository.save(banner);
            return true;
        }).orElse(false);
    }

    // 배너삭제
    @Transactional
    public boolean deleteBannersByIds(List<Long> ids) {
        try {
            bannerRepository.deleteAllByIdIn(ids);
            return true;
        } catch (Exception e) {
            // 예외 로그 처리
            System.err.println("Error deleting banners: " + e.getMessage());
            return false;
        }
    }
}
