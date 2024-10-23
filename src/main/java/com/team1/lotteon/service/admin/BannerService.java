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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class BannerService {

    private final BannerRepository bannerRepository;
    private final ModelMapper modelMapper;

    // 배너 등록

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir; // YAML에서 설정한 파일 업로드 경로


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

    public void saveBannerDetails(BannerDTO bannerDTO) {
        log.info("Sav image????????????????????");
        Banner banner = modelMapper.map(bannerDTO, Banner.class);

        bannerRepository.save(banner);
    }


    public List<BannerDTO> getAllBanners() {

        List<Banner> banners = bannerRepository.findAll();

        List<BannerDTO> bannerDTOList = banners.stream().map(banner -> modelMapper.map(banner, BannerDTO.class)).collect(Collectors.toList());
        return bannerDTOList;
    }


}
