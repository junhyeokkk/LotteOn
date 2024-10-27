package com.team1.lotteon.service.admin;

import com.team1.lotteon.dto.BannerDTO;
import com.team1.lotteon.dto.ConfigDTO;
import com.team1.lotteon.entity.Banner;
import com.team1.lotteon.entity.Config;
import com.team1.lotteon.repository.BannerRepository;
import com.team1.lotteon.repository.ConfigRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
    날짜 : 2024/10/22
    이름 : 최준혁
    내용 : config 서비스 생성

    수정내역
    - 전체 정보 조회 (10/22)
    - 사이트 정보 수정 (10/22)
    - 회사 정보 수정 (10/22)
    - 카피라이트 정보 수정 (10/22)
    - 파일 업로드 (헤더, 푸터, 파비콘) (10/23)
*/

@Log4j2
@RequiredArgsConstructor
@Service
public class ConfigService {

    private final ConfigRepository configRepository;
    private final BannerRepository bannerRepository;
    private final ModelMapper modelMapper;

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir; // YAML에서 설정한 파일 업로드 경로


    // 전체 정보 조회 (1개 밖에 없어서 아이디를 고정해줬지만 추후 develop 생각) (준혁)
    public ConfigDTO getCompanyInfo() {

        Config config = configRepository.findById(1).orElseThrow(() -> new EntityNotFoundException("Config not found"));

        return modelMapper.map(config, ConfigDTO.class);
    }

    // 사이트 정보 수정 (준혁)
    public void updateSiteInfo(String title, String subTitle) {
        Config config = configRepository.findById(1)  // Assume there's only one config record
                .orElseThrow(() -> new RuntimeException("Config not found"));

        if (title != null) {
            config.setTitle(title);
        }
        if (subTitle != null) {
            config.setSub_title(subTitle);
        }

        configRepository.save(config);
    }

    // 회사 정보 수정 (준혁)
    public void updateComInfo(String b_name, String ceo, String b_num,
                             String b_report, String addr1, String addr2) {
        Config config = configRepository.findById(1)  // Assume there's only one config record
                .orElseThrow(() -> new RuntimeException("Config not found"));

        if (b_name != null) {
            config.setB_name(b_name);
        }
        if (ceo != null) {
            config.setCeo(ceo);
        }
        if (b_num != null) {
            config.setB_num(b_num);
        }
        if (b_report != null) {
            config.setB_report(b_report);
        }
        if (addr1 != null) {
            config.setAddr1(addr1);
        }
        if (addr2 != null) {
            config.setAddr2(addr2);
        }

        configRepository.save(config);
    }

    // 고객센터 정보 수정 (준혁)
    public void updateCsInfo(String cs_num, String cs_time, String cs_email, String dispute ) {
        Config config = configRepository.findById(1)  // Assume there's only one config record
                .orElseThrow(() -> new RuntimeException("Config not found"));

        if (cs_num != null) {
            config.setCs_num(cs_num);
        }
        if (cs_time != null) {
            config.setCs_time(cs_time);
        }
        if (cs_email != null) {
            config.setCs_email(cs_email);
        }
        if (dispute != null) {
            config.setDispute(dispute);
        }

        configRepository.save(config);
    }

    // 카피라이트 정보 수정 (준혁)
    public void updateCopyInfo(String copyright) {
        Config config = configRepository.findById(1)  // Assume there's only one config record
                .orElseThrow(() -> new RuntimeException("Config not found"));

        if (copyright != null) {
            config.setCopyright(copyright);
        }
        configRepository.save(config);
    }

    // 파일 업로드 (준혁)
    public void uploadLogos(MultipartFile headerLogo, MultipartFile footerLogo, MultipartFile favicon) throws IOException {
        // 파일 저장 경로 설정

        // 파일 업로드 경로 파일 객체 생성
        File fileUploadPath = new File(uploadDir+"/logo");

        // 파일 업로드 시스템 경로 구하기
        String logouploadDir = fileUploadPath.getAbsolutePath();

        log.info("adsfffffffffffff" + logouploadDir);

        if (!Files.exists(Paths.get(logouploadDir))) {
            Files.createDirectories(Paths.get(logouploadDir));
        }

        // 파일 저장
        if (!headerLogo.isEmpty()) {
            headerLogo.transferTo(new File( logouploadDir, headerLogo.getOriginalFilename()));
        }
        if (!footerLogo.isEmpty()) {
            footerLogo.transferTo(new File( logouploadDir, footerLogo.getOriginalFilename()));
        }
        if (!favicon.isEmpty()) {
            favicon.transferTo(new File( logouploadDir, favicon.getOriginalFilename()));
        }

        // 로고 파일명을 DB에 업데이트
        Config existingConfig = configRepository.findById(1) // 적절한 ID 가져오기
                .orElseThrow(() -> new RuntimeException("Config not found"));
        existingConfig.setHeaderlogo(headerLogo.getOriginalFilename());
        existingConfig.setFooterlogo(footerLogo.getOriginalFilename());
        existingConfig.setFavicon(favicon.getOriginalFilename());
        configRepository.save(existingConfig);
    }

}
