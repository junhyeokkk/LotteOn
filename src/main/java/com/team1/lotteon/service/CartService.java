package com.team1.lotteon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.lotteon.dto.cart.CartDTO;
import com.team1.lotteon.dto.cart.CartRequestDTO;
import com.team1.lotteon.entity.Cart;
import com.team1.lotteon.entity.Member;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import com.team1.lotteon.repository.CartRepository;
import com.team1.lotteon.repository.ProductOptionCombinationRepository;
import com.team1.lotteon.security.MyUserDetails;
import com.team1.lotteon.util.MemberUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 *   날짜 : 2024/10/29
 *   이름 : 최준혁
 *   내용 : 카트 서비스 생성
 *
 *  - 수정내역
 *  - insert 추가 (10/29 준혁)
 *  - mycartselect 추가 (10/30 준혁)
 */
@Log4j2
@RequiredArgsConstructor
@Service
public class CartService {

    private final ProductOptionCombinationRepository productOptionCombinationRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;

    // json 문자열 가공
    public String formatOptionValueCombination(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, String> options = mapper.readValue(json, new TypeReference<Map<String, String>>() {});
            return options.entrySet().stream()
                    .map(entry -> entry.getKey() + ": " + entry.getValue())
                    .collect(Collectors.joining(", "));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ""; // 파싱 실패 시 빈 문자열 반환
        }
    }

    // 로그인 한 사용자 cart select
    public List<CartDTO> getCartItemsByMemberId(String memberId) {

        if(memberId == null){
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        List<Cart> Cart = cartRepository.findByMemberUid(memberId);

        return Cart.stream()
                .map(cart -> modelMapper.map(cart, CartDTO.class))
                .collect(Collectors.toList());
    }

    // 장바구니 insert (준혁)
    @Transactional
    public boolean insertToCart(CartRequestDTO cartRequestDTO) {
        // 로그인한 사용자 조회
        Member loginMember = MemberUtil.getLoggedInMember();

        log.info("로그인 유저 있나? " + loginMember);
        if(loginMember == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        // combinationId로 ProductOptionCombination 조회 >> 데이터 무결성 및 보안을 위해 한번 더 조회
        ProductOptionCombination optionCombination = productOptionCombinationRepository.findById(cartRequestDTO.getCombinationId())
                .orElseThrow(() -> new IllegalArgumentException("선택한 옵션 조합이 유효하지 않습니다."));
                log.info("옵션조합있어?  " + optionCombination);
        // 요청한 수량이 재고보다 많은지 확인
        if (cartRequestDTO.getQuantity() > optionCombination.getStock()) {
            throw new IllegalArgumentException("선택한 옵션 조합의 재고가 부족합니다.");
        }

        // 장바구니 항목 생성
       Cart cartItem = Cart.builder()
                .member(loginMember)
                .product(optionCombination.getProduct())
                .productOptionCombination(optionCombination)  // 수정: ProductOptionCombination 객체 직접 설정
                .quantity(cartRequestDTO.getQuantity())
                .totalPrice(cartRequestDTO.getTotalPrice())
                .build();

        cartRepository.save(cartItem);
        return true;
    }

    // cartId를 통해 Cart 엔티티를 조회하고 CartDTO로 변환
    public CartDTO getCartItemById(Long cartId) {
        return cartRepository.findById(Math.toIntExact(cartId))
                .map(this::convertToCartDTO)  // 엔티티 -> DTO 변환
                .orElseThrow(() -> new IllegalArgumentException("해당 장바구니 항목을 찾을 수 없습니다: " + cartId));
    }

    // Cart 엔티티 -> CartDTO 변환 메서드
    private CartDTO convertToCartDTO(Cart cart) {
        return CartDTO.builder()
                .id(cart.getId())
                .member(cart.getMember())
                .product(cart.getProduct())
                .quantity(cart.getQuantity())
                .totalPrice(cart.getTotalPrice())
                .productOptionCombination(cart.getProductOptionCombination())
                .formattedOptions(formatOptionValueCombination(cart.getProductOptionCombination().getOptionValueCombination()))
                .build();
    }

}
