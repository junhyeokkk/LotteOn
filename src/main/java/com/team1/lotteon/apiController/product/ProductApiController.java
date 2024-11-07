package com.team1.lotteon.apiController.product;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.lotteon.dto.cart.CartDTO;
import com.team1.lotteon.dto.order.CarttoOrderRequestDTO;
import com.team1.lotteon.dto.order.OrderInfoDTO;
import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.product.ProductCreateDTO;
import com.team1.lotteon.dto.product.ProductDTO;
import com.team1.lotteon.dto.product.ProductSummaryResponseDTO;
import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import com.team1.lotteon.service.CartService;
import com.team1.lotteon.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 상품을 관리하는 api controller 생성

    - 수정내역
    - 상품 등록 메서드 수정 (준혁)
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductApiController {
    private static final Logger log = LogManager.getLogger(ProductApiController.class);
    private final ProductService productService;
    private final ObjectMapper objectMapper;
    private final CartService cartService;

    // 바로구매 세션처리 바로구매에서는 어차피 OrderInfoDTO 한객체니까 리스트 만들어서 세션 삽입
    // 여기선 조합 번호만 들어가도록!
    @PostMapping("/save-order-info")
    public ResponseEntity<?> saveOrderInfoToSession(@RequestBody OrderInfoDTO orderInfo, HttpSession session) {
        log.info("saveOrderInfoToSession 호출됨");
        log.info("orderInfo: {}", orderInfo.toString());

        // 조합 ID가 null이 아닐 경우에만 조합 객체를 설정
        if (orderInfo.getCombinationId() != null) {
            ProductOptionCombination combination = productService.getOptionCombinationById(orderInfo.getCombinationId());
            orderInfo.setProductOptionCombination(combination);
        } else {
            log.warn("combinationId가 null입니다. 옵션 없는 상품으로 간주합니다.");
        }

        // 새로운 리스트에 orderInfo 추가
        List<OrderInfoDTO> orderInfoList = new ArrayList<>();
        orderInfoList.add(orderInfo);

        // 리스트를 세션에 저장
        session.setAttribute("orderInfoList", orderInfoList);

        // 세션에서 리스트를 가져옴
        List<OrderInfoDTO> orderInfoList1 = (List<OrderInfoDTO>) session.getAttribute("orderInfoList");

        if (orderInfoList1 == null || orderInfoList1.isEmpty()) {
            return ResponseEntity.badRequest().body("세션에 orderInfoList가 없습니다.");
        }

        // 리스트를 JSON 형태로 반환
        return ResponseEntity.ok(orderInfoList1);
    }


    // 장바구니 -> 주문하기 세션처리
    @PostMapping("/prepareOrder")
    public ResponseEntity<?> prepareOrder(@RequestBody CarttoOrderRequestDTO orderRequest, HttpSession session) {
        List<Long> selectedCartIds = orderRequest.getSelectedCartIds();

        // 선택된 CartDTO 항목을 OrderInfoDTO로 변환
        List<OrderInfoDTO> orderInfoList = selectedCartIds.stream()
                .map(cartService::getCartItemById) // CartDTO 가져오기
                .map(this::convertToOrderInfoDTO)   // CartDTO -> OrderInfoDTO 변환
                .collect(Collectors.toList());

        session.setAttribute("orderInfoList", orderInfoList);  // 세션에 저장
        return ResponseEntity.ok().build();
    }

    // CartDTO -> OrderInfoDTO 변환 메서드
    private OrderInfoDTO convertToOrderInfoDTO(CartDTO cart) {
        ProductOptionCombination optionCombination = cart.getProductOptionCombination();
        Long combinationId = optionCombination != null ? optionCombination.getId() : null;
        String formattedOptions = cart.getFormattedOptions();

        return OrderInfoDTO.builder()
                .productId(cart.getProduct().getId())
                .productImg(cart.getProduct().getProductImg1())
                .productName(cart.getProduct().getProductName())
                .productDescription(cart.getProduct().getDescription())
                .quantity(cart.getQuantity())
                .originalPrice(cart.getProduct().getPrice())
                .discountRate(cart.getProduct().getDiscountRate())
                .discountedPrice((cart.getProduct().getPrice() * cart.getQuantity()) - cart.getTotalPrice()) // 할인된 가격
                .points(cart.getProduct().getPoint())
                .deliveryFee(cart.getProduct().getDeliveryFee())
                .total(cart.getTotalPrice())
                .productOptionCombination(optionCombination)  // 옵션 조합 객체 그대로 사용 (null 가능)
                .combinationId(combinationId) // 옵션 조합 ID, 없으면 null
                .formattedOptions(formattedOptions != null ? formattedOptions : "옵션 없음") // 옵션 문자열이 없으면 기본 텍스트
                .cartId(cart.getId()) // 카트 삭제를 위한 카트 id
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> createProduct(
            @ModelAttribute ProductCreateDTO productCreateDTO,
            @RequestParam(value = "file1", required = false) MultipartFile file1,
            @RequestParam(value = "file2", required = false) MultipartFile file2,
            @RequestParam(value = "file3", required = false) MultipartFile file3
    ) {
        log.info("잘 바인딩 되니? " + productCreateDTO.toString());

        try {
            // 파일 업로드 처리 및 저장
            String imgPath1 = file1 != null ? productService.saveFile(file1) : null;
            String imgPath2 = file2 != null ? productService.saveFile(file2) : null;
            String imgPath3 = file3 != null ? productService.saveFile(file3) : null;

            // 이미지 경로를 DTO에 설정
            productCreateDTO.setProductImg1(imgPath1);
            productCreateDTO.setProductImg2(imgPath2);
            productCreateDTO.setProductImg3(imgPath3);

            // 서비스 호출을 통해 제품 생성
            Product createdProduct = productService.saveProduct(productCreateDTO);

            return ResponseEntity.ok(Collections.singletonMap("message", "Product created successfully"));


        } catch (IOException e) {
            throw new RuntimeException("JSON 처리 중 오류 발생: " + e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<PageResponseDTO<ProductSummaryResponseDTO>> getProducts(@PageableDefault Pageable pageable) {
        System.out.println("pageable = " + pageable);
        PageResponseDTO<ProductSummaryResponseDTO> products = productService.getProducts(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }
}
