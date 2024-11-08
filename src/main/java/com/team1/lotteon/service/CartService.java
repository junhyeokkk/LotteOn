package com.team1.lotteon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.lotteon.dto.cart.CartDTO;
import com.team1.lotteon.dto.cart.CartRequestDTO;
import com.team1.lotteon.entity.Cart;
import com.team1.lotteon.entity.Member;
import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import com.team1.lotteon.repository.CartRepository;
import com.team1.lotteon.repository.ProductOptionCombinationRepository;
import com.team1.lotteon.repository.ProductRepository;
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
    private final ProductRepository productRepository;

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
        if (loginMember == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        Product product = null;
        ProductOptionCombination optionCombination = null;
        Cart existingCartItem = null;

        if (cartRequestDTO.getCombinationId() != null) {
            // 옵션이 있는 상품의 경우 >> ProductOptionCombination 조회 및 재고 확인
            optionCombination = productOptionCombinationRepository.findById(cartRequestDTO.getCombinationId())
                    .orElseThrow(() -> new IllegalArgumentException("선택한 옵션 조합이 유효하지 않습니다."));
            log.info("옵션 조합 있나?  " + optionCombination);

            // 기존에 동일한 상품+옵션 조합이 장바구니에 있는지 확인
            existingCartItem = cartRepository.findByMemberAndProductAndProductOptionCombination(
                            loginMember, optionCombination.getProduct(), optionCombination)
                    .orElse(null); // Optional을 Cart로 변환

            // 요청한 수량과 기존 수량의 합이 재고를 초과하는지 확인
            if (cartRequestDTO.getQuantity() + (existingCartItem != null ? existingCartItem.getQuantity() : 0) > optionCombination.getStock()) {
                throw new IllegalArgumentException("선택한 옵션 조합의 재고가 부족합니다.");
            }

            product = optionCombination.getProduct();
        } else {
            // 옵션이 없는 경우 >> Product 조회
            product = productRepository.findById(cartRequestDTO.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품 ID가 유효하지 않습니다."));
            log.info("옵션이 없는 상품: " + product);

            // 기존에 동일한 상품이 장바구니에 있는지 확인 (옵션 없는 경우)
            existingCartItem = cartRepository.findByMemberAndProductAndProductOptionCombinationIsNull(loginMember, product)
                    .orElse(null); // Optional을 Cart로 변환

            // 옵션이 없는 경우 요청한 수량과 기존 수량의 합이 재고를 초과하는지 확인
            if (cartRequestDTO.getQuantity() + (existingCartItem != null ? existingCartItem.getQuantity() : 0) > product.getStock()) {
                throw new IllegalArgumentException("해당 상품의 재고가 부족합니다.");
            }
        }

        if (existingCartItem != null) {
            // 동일한 상품이나 상품+옵션 조합이 이미 장바구니에 있는 경우 수량 및 총 가격 업데이트
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartRequestDTO.getQuantity());
            existingCartItem.setTotalPrice(existingCartItem.getTotalPrice() + cartRequestDTO.getTotalPrice());
        } else {
            // 장바구니에 새로운 항목 추가
            Cart cartItem = Cart.builder()
                    .member(loginMember)
                    .product(product)
                    .productOptionCombination(optionCombination) // 옵션이 없는 상품의 경우 null
                    .quantity(cartRequestDTO.getQuantity())
                    .totalPrice(cartRequestDTO.getTotalPrice())
                    .build();

            if(optionCombination != null){
                cartItem.setOptionCombinationSnapshot(optionCombination.getOptionValueCombination());  // 장바구니 조합 정보 스냅샷
            }
            cartRepository.save(cartItem);
        }

        return true;
    }

    // cartId를 통해 Cart 엔티티를 조회하고 CartDTO로 변환
    public CartDTO getCartItemById(Long cartId) {
        return cartRepository.findById(Math.toIntExact(cartId))
                .map(this::convertToCartDTO)  // 엔티티 -> DTO 변환
                .orElseThrow(() -> new IllegalArgumentException("해당 장바구니 항목을 찾을 수 없습니다: " + cartId));
    }

    // Cart 엔티티 -> CartDTO 변환 메서드 수정
    private CartDTO convertToCartDTO(Cart cart) {
        ProductOptionCombination optionCombination = cart.getProductOptionCombination();
        String formattedOptions = optionCombination != null
                ? formatOptionValueCombination(optionCombination.getOptionValueCombination())
                : "옵션 없음";  // 옵션이 없는 경우 기본 문자열 설정

        return CartDTO.builder()
                .id(cart.getId())
                .member(cart.getMember())
                .product(cart.getProduct())
                .quantity(cart.getQuantity())
                .totalPrice(cart.getTotalPrice())
                .productOptionCombination(optionCombination) // 옵션이 없으면 null
                .formattedOptions(formattedOptions) // 포맷된 옵션 문자열을 설정
                .build();
    }


}
