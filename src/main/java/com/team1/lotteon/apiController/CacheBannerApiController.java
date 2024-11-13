package com.team1.lotteon.apiController;

import com.team1.lotteon.dto.BannerDTO;
import com.team1.lotteon.entity.Banner;
import com.team1.lotteon.service.admin.BannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/*
    날짜 : 2024/11/11
    이름 : 최준혁
    내용 : 배너 캐싱 처리 및 출력을 위한 api controller 생성
*/
@Log4j2
@RequiredArgsConstructor
@RestController
public class CacheBannerApiController {

    private final BannerService bannerService;
    private final ModelMapper modelMapper;

    @GetMapping("/api/banner/main")
    public List<BannerDTO> getMainBanners() {
        List<Banner> mainBanners = bannerService.getBannersByPosition("Main");

        log.info("배너ㅓㅓㅓ " + mainBanners);
        return mainBanners.stream()
                .map(banner -> modelMapper.map(banner, BannerDTO.class))
                .collect(Collectors.toList());
    }
    @GetMapping("/api/banner/slider")
    public List<BannerDTO> getSliderBanners() {
        List<Banner> banners = bannerService.getBannersByPosition("Silder");
        return banners.stream()
                .map(banner -> modelMapper.map(banner, BannerDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/banner/product")
    public List<BannerDTO> getProductBanners() {
        List<Banner> banners = bannerService.getBannersByPosition("Product");
        return banners.stream()
                .map(banner -> modelMapper.map(banner, BannerDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/banner/mypage")
    public List<BannerDTO> getMyPageBanners() {
        log.info("asfdfdas");
        List<Banner> banners = bannerService.getBannersByPosition("Mypage");
        log.info("mypage" + banners);
        return banners.stream()
                .map(banner -> modelMapper.map(banner, BannerDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/banner/login")
    public List<BannerDTO> getLoginBanners() {
        log.info("ddddd");
        List<Banner> banners = bannerService.getBannersByPosition("Login");
        log.info("Login" + banners);
        return banners.stream()
                .map(banner -> modelMapper.map(banner, BannerDTO.class))
                .collect(Collectors.toList());
    }
}
