package com.team1.lotteon.apiController.cart;

import com.team1.lotteon.dto.cart.CartRequestDTO;
import com.team1.lotteon.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/*
     날짜 : 2024/10/29
     이름 : 최준혁
     내용 : 카트 api 컨트롤러 생성
*/
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartApiController {

    private final CartService cartService;

    // 장바구니 insert (준혁)
    @PostMapping("/insert")
    public ResponseEntity<?> insertToCart(@RequestBody CartRequestDTO cartRequest) {
        try {
            log.info("Inserting cart to cart");
            boolean success = cartService.insertToCart(cartRequest);

            if (success) {
                return ResponseEntity.ok().body(Collections.singletonMap("success", true));
            } else {
                return ResponseEntity.badRequest().body(Collections.singletonMap("success", false));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }


}
