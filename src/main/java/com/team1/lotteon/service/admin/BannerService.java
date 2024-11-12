package com.team1.lotteon.service.admin;

import com.team1.lotteon.dto.BannerDTO;
import com.team1.lotteon.entity.Banner;
import com.team1.lotteon.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    //배너 실행 여부 변경
    public void changeBannerStatues() {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS); // 초 단위까지 자르기
        List<Banner> banners = bannerRepository.findAll();
        for (Banner banner : banners) {
            LocalDate startDate = banner.getDisplayStartDate();
            LocalTime startTime = banner.getDisplayStartTime().truncatedTo(ChronoUnit.SECONDS); // 초 단위까지 자르기
            LocalDate endDate = banner.getDisplayEndDate();
            LocalTime endTime = banner.getDisplayEndTime().truncatedTo(ChronoUnit.SECONDS); // 초 단위까지 자르기

            boolean shouldActivate = (currentDate.isAfter(startDate) || currentDate.isEqual(startDate)) &&
                    (currentTime.isAfter(startTime) || currentTime.equals(startTime));
            boolean shouldDeactivate = (currentDate.isAfter(endDate) || currentDate.isEqual(endDate)) &&
                    (currentTime.isAfter(endTime) || currentTime.equals(endTime));

            log.info("Banner activation check: " + shouldActivate);
            log.info("Banner deactivation check: " + shouldDeactivate);

            if (shouldActivate && !banner.isActive()) {
                banner.setIsActive(true);
                bannerRepository.save(banner);
            } else if (shouldDeactivate && banner.isActive()) {
                banner.setIsActive(false);
                bannerRepository.save(banner);
            }
        }

    }
}
