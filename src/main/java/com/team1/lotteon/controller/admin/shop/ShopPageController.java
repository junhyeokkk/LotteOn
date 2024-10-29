package com.team1.lotteon.controller.admin.shop;
/*
     날짜 : 2024/10/21
     이름 : 최준혁
     내용 : 등록된 상점 출력

     수정이력
      - 2024/10/28 이도영 - 상점 목록 출력
*/
import com.team1.lotteon.dto.ShopDTO;
import com.team1.lotteon.dto.pageDTO.NewPageRequestDTO;
import com.team1.lotteon.dto.pageDTO.NewPageResponseDTO;
import com.team1.lotteon.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
public class ShopPageController {

    private final ShopService shopService;

    @GetMapping("/admin/shop/list")
    public String list(@RequestParam(required = false) String type,
                       @RequestParam(required = false) String keyword,
                       NewPageRequestDTO newPageRequestDTO, Model model) {

        // 검색 조건 설정
        newPageRequestDTO.setType(type);
        newPageRequestDTO.setKeyword(keyword);

        NewPageResponseDTO<ShopDTO> shopdto = shopService.selectshopAll(newPageRequestDTO);
        model.addAttribute("shopdtos", shopdto);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        return "admin/shop/list";
    }
    @DeleteMapping("/admin/shop/delete")
    public ResponseEntity<Void> deleteShops(@RequestBody List<Long> ids) {
        shopService.deleteShops(ids);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/admin/shop/sales")
    public String sales(){

        return "admin/shop/sales";
    }
}
