package com.team1.lotteon.controller.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.lotteon.dto.GeneralMemberDTO;
import com.team1.lotteon.dto.order.FinalOrderSummaryDTO;
import com.team1.lotteon.dto.order.OrderInfoDTO;
import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.cart.CartDTO;
import com.team1.lotteon.dto.category.CategoryResponseDTO;
import com.team1.lotteon.dto.order.OrderSummaryDTO;
import com.team1.lotteon.dto.product.ProductDTO;
import com.team1.lotteon.dto.product.ProductSummaryResponseDTO;
import com.team1.lotteon.entity.GeneralMember;
import com.team1.lotteon.security.MyUserDetails;
import com.team1.lotteon.service.CartService;
import com.team1.lotteon.service.CategoryService;
import com.team1.lotteon.service.Order.OrderService;
import com.team1.lotteon.service.ProductService;
import com.team1.lotteon.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 상품 관련 페이지 컨트롤러 생성
*/
@Log4j2
@Controller
@RequiredArgsConstructor
public class ProductPageController {
    private final UserService userService;
    private final ProductService productService;
    private final CartService cartService;
    private final CategoryService categoryService;

    private final ModelMapper modelMapper;
    private final OrderService orderService;

    // list 페이지 이동
    @GetMapping("/product/list")
    public String list(Model model, @PageableDefault Pageable pageable, @RequestParam(required = false) Long categoryId) {
        System.out.println("categoryId = " + categoryId);
        PageResponseDTO<ProductSummaryResponseDTO> productsPage = null;

        if (categoryId == null) {
            productsPage = productService.getProducts(pageable);
        } else {
            productsPage = productService.getProductsByCategoryId(categoryId, pageable);
            List<CategoryResponseDTO> parentList = categoryService.getParentList(categoryId);
            model.addAttribute("parentList", parentList);
        }

        model.addAttribute("productsPage", productsPage);
        model.addAttribute("categoryId", categoryId);

        return "product/list";
    }

    // view 페이지 이동
    @GetMapping("/product/view")
    public String view(Model model) {
        log.info("view");

        return "product/view";
    }

    // complete 페이지 이동
    @GetMapping("/product/complete")
    public String complete(Model model) {
        log.info("complete");

        return "product/complete";
    }

    // 주문성공시 주문 요약정보 들고 complete 페이지 이동
    @GetMapping("/product/complete/{orderId}")
    public String complete(Model model, @PathVariable Long orderId) {
        log.info("complete");
        log.info("Fetching order summary for order ID: {}", orderId);

        // 주문 ID로 주문 정보 조회
        OrderSummaryDTO orderSummary = orderService.getOrderSummary(orderId);

        log.info("Fetched order summary for order ID: {}", orderSummary.toString());
        log.info("orderitems : " + orderSummary.getOrderItems().toString());

        model.addAttribute("orderSummary", orderSummary);

        return "product/complete";
    }

    // order 페이지 이동
    @GetMapping("/product/order")
    public String order(Model model, HttpSession session, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        log.info("order");
        if(myUserDetails == null){
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        GeneralMemberDTO member = modelMapper.map(myUserDetails.getGeneralMember(), GeneralMemberDTO.class);

        List<OrderInfoDTO> sessionOrderInfoList = (List<OrderInfoDTO>) session.getAttribute("orderInfoList");

        if (sessionOrderInfoList == null || sessionOrderInfoList.isEmpty()) {
            log.warn("세션에 orderInfoList가 없습니다.");
            return "redirect:/product";
        }

        // 최종 결제 정보 합산을 위한 변수 초기화
        FinalOrderSummaryDTO finalOrderSummary = new FinalOrderSummaryDTO();
        int totalOriginalPrice = 0, totalDiscount = 0, totalOrderAmount = 0, totalDeliveryFee = 0, totalPoints = 0, totalEarnedPoints = 0, totalQuantity = 0;

        // 한 번의 반복문에서 처리
        for (OrderInfoDTO orderInfo : sessionOrderInfoList) {
            // 1. 이미지 경로 가공
            String productImg = orderInfo.getProductImg();
            if (productImg != null && !productImg.startsWith("http")) {
                productImg = "http://localhost:8080/uploads/product/" + productImg;
                orderInfo.setProductImg(productImg);
            }

            // 2. 옵션 문자열 포맷팅
            if (orderInfo.getFormattedOptions() == null) {
                String formattedOptions = cartService.formatOptionValueCombination(orderInfo.getProductOptionCombination().getOptionValueCombination());
                orderInfo.setFormattedOptions(formattedOptions);
            }

            // 3. 최종 결제 정보 합산
            totalQuantity += orderInfo.getQuantity();
            totalOriginalPrice += orderInfo.getOriginalPrice() * orderInfo.getQuantity();
            totalOrderAmount += orderInfo.getTotal();
            totalDiscount += (orderInfo.getDiscountedPrice());
            totalDeliveryFee += orderInfo.getDeliveryFee();
            totalPoints += orderInfo.getPoints();
            totalEarnedPoints += orderInfo.getPoints();
        }

        // FinalOrderSummaryDTO에 합산 값 설정
        finalOrderSummary.setTotalQuantity(totalQuantity);
        finalOrderSummary.setTotalOriginalPrice(totalOriginalPrice);
        finalOrderSummary.setTotalDiscount(totalDiscount);
        finalOrderSummary.setTotalOrderAmount(totalOrderAmount);
        finalOrderSummary.setTotalDeliveryFee(totalDeliveryFee > 0 ? totalDeliveryFee : 0);
        finalOrderSummary.setTotalEarnedPoints(totalEarnedPoints);

        // 모델에 추가
        model.addAttribute("orderInfoList", sessionOrderInfoList);
        model.addAttribute("finalOrderSummary", finalOrderSummary);
        model.addAttribute("member", member);

        log.info("가공된 이미지 경로 포함된 orderInfo: {}", sessionOrderInfoList);

        return "product/order";
    }


    // cart 페이지 이동
    @GetMapping("/product/cart")
    public String cart(Model model, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        log.info("Accessing cart");

        if (myUserDetails == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        String memberId = myUserDetails.getUsername();

        // 로그인 한 객체 id 값 넣어주고 service 호출
        List<CartDTO> cartItems = cartService.getCartItemsByMemberId(memberId);

        // 문자열 가공
        for (CartDTO cartItem : cartItems) {
            if (cartItem.getProductOptionCombination() != null) {
                // 옵션이 있는 경우 옵션 값 포맷팅
                String formattedOptions = cartService.formatOptionValueCombination(cartItem.getProductOptionCombination().getOptionValueCombination());
                cartItem.setFormattedOptions(formattedOptions);  // CartDTO에 formattedOptions 필드 추가 필요
            } else {
                // 옵션이 없는 경우 빈 문자열 할당
                cartItem.setFormattedOptions("");  // 또는 "옵션 없음"과 같은 표시를 원할 시 해당 문자열로 설정
            }
        }

        log.info("내 카트 " + cartItems.toString());

        // 모델 참조
        model.addAttribute("cartItems", cartItems);

        return "product/cart";
    }



    // view 페이지 이동
    @GetMapping("/product/view/{id}")
    public String viewProduct(@PathVariable("id") Long id, Model model) throws JsonProcessingException {
        log.info("컨트롤러 ㅇㅇㅇ");
        ProductDTO saveProduct = productService.getProductById(id);

        if (saveProduct == null) {
            return "error/404"; // 상품이 없는 경우, 404 페이지로 이동
        }

        log.info("찾아온 상품" + saveProduct.toString());
        log.info("찾아온 상품 옵션들" + saveProduct.getProductOptions().toString());
        log.info("찾아온 상품 상세들" + saveProduct.getProductDetails().toString());

        // Java 컨트롤러 내 optionCombinations 설정
        Map<String, Map<String, Object>> optionCombinations = new HashMap<>();

        saveProduct.getProductOptionCombinations().forEach(combination -> {
            try {
                // `optionIdCombination` JSON 문자열을 파싱하여 Map으로 변환
                Map<String, Integer> combinationMap = new ObjectMapper().readValue(
                        combination.getOptionIdCombination(), Map.class);

                // 조합 Map에서 값들만 추출하여 정렬된 리스트로 변환
                List<Integer> optionValues = new ArrayList<>(combinationMap.values());
                optionValues.sort(Integer::compareTo);

                // 배열 형식의 문자열로 변환하여 키로 사용 (예: "[17,20]")
                String jsonKey = new ObjectMapper().writeValueAsString(optionValues);

                // 조합에 대해 `combinationId`와 `stock`을 저장할 Map 생성
                Map<String, Object> detailsMap = new HashMap<>();
                detailsMap.put("combinationId", combination.getId());
                detailsMap.put("stock", combination.getStock());

                // `optionCombinations`에 간소화된 키와 상세 정보를 추가
                optionCombinations.put(jsonKey, detailsMap);

            } catch (JsonProcessingException e) {
                log.error("JSON 파싱 오류", e);
            }
        });
        int discountedPrice = (int) (saveProduct.getPrice() * (1 - saveProduct.getDiscountRate() / 100.0));

        saveProduct.setDiscountedPrice(discountedPrice);

        // 기본 재고 수량을 모델에 추가 (옵션이 없는 경우 참조)
        model.addAttribute("defaultStock", saveProduct.getStock());
        // 모델에 상품과 옵션 조합 추가
        model.addAttribute("product", saveProduct);
        // 모델에 JSON 형식으로 변환된 데이터를 추가
        model.addAttribute("optionCombinations", optionCombinations);

        return "product/view"; // 뷰 페이지로 이동
    }

//    @GetMapping("/product/{cate}")
//    public String index(@PathVariable String cate, Model model, @PageableDefault Pageable pageable) {
//
//        log.info("product/{} 접속", cate);
//        log.info(cate);
//        model.addAttribute("cate", cate);
//
//        if(cate != null && cate.equals("list"))
//        {
//            PageResponseDTO<ProductSummaryResponseDTO> productsPage = productService.getProducts(pageable);
//            model.addAttribute("productsPage", productsPage);
//        }
//
//        return "product/layout/product_layout";
//    }


}
