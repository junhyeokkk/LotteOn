package com.team1.lotteon.controller.admin.config;

import com.team1.lotteon.dto.BannerDTO;
import com.team1.lotteon.dto.ConfigDTO;
import com.team1.lotteon.dto.CouponDTO;
import com.team1.lotteon.service.admin.BannerService;
import com.team1.lotteon.service.admin.ConfigService;
import com.team1.lotteon.dto.VersionDTO;
import com.team1.lotteon.dto.category.CategoryWithChildrenResponseDTO;
import com.team1.lotteon.service.CategoryService;
import com.team1.lotteon.service.admin.CouponService;
import com.team1.lotteon.service.admin.VersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
public class ConfigPageController {

    private final ConfigService configService;
    private final BannerService bannerService;
    private final CategoryService categoryService;
    private final VersionService versionService;
    private final CouponService couponService;

    // info 페이지 이동시 config 정보 담기
    @GetMapping("/admin/config/info")
    public String info(Model model){

        ConfigDTO config = configService.getCompanyInfo();
        model.addAttribute("config", config);

        return "admin/config/info";
    }

//    @GetMapping("/admin/config/policy")
//    public String policy(){
//
//        return "admin/config/policy";
//    }

    @GetMapping("/admin/config/version")
    public String version(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<VersionDTO> versionPage = versionService.getAllVersions(page, 10); // 현재 페이지와 10개 가져오기
        model.addAttribute("versions", versionPage.getContent()); // 현재 페이지의 버전 데이터
        model.addAttribute("currentPage", page); // 현재 페이지 번호
        model.addAttribute("totalPages", versionPage.getTotalPages()); // 총 페이지 수
        return "admin/config/version"; // 뷰 이름
    }


    @GetMapping("/admin/config/banner")
    public String banner(){

        return "admin/config/banner"; // 해당 HTML 페이지로 이동
    }

    @GetMapping("/admin/config/category")
    public String category(Model model){
        List<CategoryWithChildrenResponseDTO> allRootCategories = categoryService.getAllRootCategories().getData();
        model.addAttribute("categories", allRootCategories);
        System.out.println("allRootCategories = " + allRootCategories);
        return "admin/config/category";
    }

    //쿠폰 (유정)
    @GetMapping("/admin/config/coupon")
    public String coupon(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "") String keyword,
                         @RequestParam(defaultValue = "num") String type,
                         Model model) {
        //Page<CouponDTO> couponPage = couponService.getAllVersions(page, 10); // 현재 페이지와 10개 가져오기
        //model.addAttribute("coupons", couponPage.getContent()); // 현재 페이지의 버전 데이터
        //model.addAttribute("currentPage", page); // 현재 페이지 번호
        //model.addAttribute("totalPages", couponPage.getTotalPages()); // 총 페이지 수
        return "admin/config/coupon"; // 뷰 이름
    }
}
