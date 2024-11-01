package com.team1.lotteon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.product.ProductCreateDTO;
import com.team1.lotteon.dto.product.ProductDTO;
import com.team1.lotteon.dto.product.ProductSummaryResponseDTO;
import com.team1.lotteon.dto.product.ProductdetailDTO;
import com.team1.lotteon.dto.product.productOption.ProductOptionDTO;
import com.team1.lotteon.dto.product.productOption.OptionItemDTO;
import com.team1.lotteon.dto.product.productOption.ProductOptionCombinationDTO;
import com.team1.lotteon.entity.Category;
import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.Productdetail;
import com.team1.lotteon.entity.SellerMember;
import com.team1.lotteon.entity.productOption.ProductOption;
import com.team1.lotteon.entity.productOption.OptionItem;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import com.team1.lotteon.repository.*;
import com.team1.lotteon.util.MemberUtil;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 상품 서비스 개발

    - 수정내역
    - 상품 등록 메서드 수정 (준혁)
*/
@RequiredArgsConstructor
@Service
public class ProductService {
    private static final Logger log = LogManager.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    private final ProductDetailRepository productDetailRepository;
    private final OptionRepository optionRepository;
    private final OptionItemRepository optionItemRepository;

    private final ProductOptionCombinationRepository productOptionCombinationRepository;
    private final CategoryRepository categoryRepository;

    // 상품 이미지 업로드
    @Value("${spring.servlet.multipart.location}")
    private String uploadDir; // YAML에서 설정한 파일 업로드 경로

    // 이미지 업로드 처리
    public String uploadFile(MultipartFile file) throws IOException {
        // 파일 업로드 경로 파일 객체 생성
        File fileUploadPath = new File(uploadDir + "/product");

        // 파일 업로드 시스템 경로 구하기
        String productUploadDir = fileUploadPath.getAbsolutePath();

        log.info("adsfffffffffffff" + productUploadDir);

        if (!Files.exists(Paths.get(productUploadDir))) {
            Files.createDirectories(Paths.get(productUploadDir));
        }

        // 고유한 파일명 생성 (UUID와 타임스탬프 조합)
        String fileExtension = getFileExtension(file.getOriginalFilename());  // 파일 확장자 추출
        String uniqueFileName = UUID.randomUUID().toString() + "_" + System.currentTimeMillis() + "." + fileExtension;
        File destinationFile = new File(productUploadDir + "/" + uniqueFileName);

        // 파일 저장
        file.transferTo(destinationFile);

        // 저장된 파일의 경로 반환
        return uniqueFileName;
    }

    // 파일 확장자 추출 메서드
    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    @Transactional
    public Product saveProduct(ProductCreateDTO dto) throws JsonProcessingException {
        // 카테고리 ID로 카테고리 엔티티 조회
        Category category = entityManager.getReference(Category.class, dto.getCategoryId());

        // 로그인 한 seller 멤버 객체 저장
        SellerMember seller = MemberUtil.getLoggedInSellerMember();

        // Product 엔티티 생성 및 저장
        Product product = Product.builder()
                .productName(dto.getProductName())
                .productImg1(dto.getProductImg1())
                .productImg2(dto.getProductImg2())
                .productImg3(dto.getProductImg3())
                .description(dto.getDescription())
                .manufacturer(dto.getManufacturer())
                .price(dto.getPrice())
                .discountRate(dto.getDiscountRate())
                .point(dto.getPoint())
                .stock(dto.getStock())
                .deliveryFee(dto.getDeliveryFee())
                .Status(dto.getProductStatus())
                .warranty(dto.getWarranty())
                .receiptIssued(dto.getReceiptMethod())
                .businessType(dto.getBusinessType())
                .origin(dto.getOrigin())
                .hasOptions(dto.isHasOptions())
                .category(category)
                .member(seller)
                .shop(seller.getShop())
                .build();

        productRepository.save(product);

        // 상세 정보 저장
        List<ProductdetailDTO> productDetailsList = objectMapper.readValue(
                dto.getProductDetailsJson(), new TypeReference<List<ProductdetailDTO>>() {
                });

        List<Productdetail> productDetails = new ArrayList<>();
        for (ProductdetailDTO detailDto : productDetailsList) {
            Productdetail detail = Productdetail.builder()
                    .name(detailDto.getName())
                    .value(detailDto.getValue())
                    .product(product)
                    .build();
            productDetailRepository.save(detail);
            productDetails.add(detail);
        }
        product.setProductDetails(productDetails);

        if (dto.isHasOptions()) {
            // 옵션 정보 저장
            List<ProductOptionDTO> productOptionDtoList = objectMapper.readValue(
                    dto.getOptionsJson(), new TypeReference<List<ProductOptionDTO>>() {
                    });

            List<ProductOption> productOptions = new ArrayList<>();
            for (ProductOptionDTO productOptionDto : productOptionDtoList) {
                ProductOption productOption = ProductOption.builder()
                        .name(productOptionDto.getName())
                        .product(product)
                        .build();
                optionRepository.save(productOption);
                productOptions.add(productOption);

                // 옵션 아이템 저장
                for (OptionItemDTO optionItemDto : productOptionDto.getOptionItems()) {
                    OptionItem optionItem = OptionItem.builder()
                            .value(optionItemDto.getValue())
                            .productOption(productOption)
                            .build();
                    optionItemRepository.save(optionItem);
                }
            }
            product.setProductOptions(productOptions);

            // 옵션 조합 저장
            List<ProductOptionCombinationDTO> combinationsList = objectMapper.readValue(
                    dto.getCombinationsJson(), new TypeReference<List<ProductOptionCombinationDTO>>() {
                    });

            List<ProductOptionCombination> combinations = new ArrayList<>();
            for (ProductOptionCombinationDTO combinationDto : combinationsList) {
                Map<String, Long> idCombination = new HashMap<>();
                Map<String, String> valueCombination = new HashMap<>();

                String[] optionValues = combinationDto.getOptionCombination().split(",\\s*");
                for (String optionValue : optionValues) {
                    // optionsJson을 통해 value에 해당하는 optionName을 확인
                    String optionName = productOptionDtoList.stream()
                            .filter(option -> option.getOptionItems().stream()
                                    .anyMatch(item -> item.getValue().equals(optionValue)))
                            .map(ProductOptionDTO::getName)
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Option name not found for value: " + optionValue));

                    // product, optionName, optionValue를 사용해 OptionItem을 조회
                    OptionItem optionItem = optionItemRepository.findByProductAndOptionNameAndValue(product, optionName, optionValue)
                            .orElseThrow(() -> new RuntimeException("Option item not found: " + optionName + " - " + optionValue));

                    idCombination.put(optionName, optionItem.getId());
                    valueCombination.put(optionName, optionValue);
                }

                // JSON 변환 후 ProductOptionCombination 저장
                String optionIdCombinationJson = objectMapper.writeValueAsString(idCombination);
                String optionValueCombinationJson = objectMapper.writeValueAsString(valueCombination);

                ProductOptionCombination combination = ProductOptionCombination.builder()
                        .product(product)
                        .optionIdCombination(optionIdCombinationJson)
                        .optionValueCombination(optionValueCombinationJson)
                        .stock(combinationDto.getStock())
                        .build();

                productOptionCombinationRepository.save(combination);
                combinations.add(combination);
            }
        } else {
            product.setStock(dto.getStock());
        }

        return product;
    }


    public PageResponseDTO<ProductSummaryResponseDTO> getProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return PageResponseDTO.fromPage(products.map(ProductSummaryResponseDTO::fromEntity));
    }

    public ProductDTO getProductById(Long id) {

        log.info("서비스 입성");
        Product product = productRepository.findById(id).orElse(null);

        ProductDTO savedproductDTO = modelMapper.map(product, ProductDTO.class);

        return savedproductDTO;
    }

    // 조합 문자열 파싱
    public Optional<Integer> checkStockForCombination(Product product, Map<String, String> selectedOptions) {
        // 선택된 옵션을 Map<String, Integer> 형식으로 변환
        Map<String, Integer> selectedOptionsIdMap = selectedOptions.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> Integer.parseInt(entry.getValue())));

        // 각 ProductOptionCombination의 `optionIdCombination`을 파싱해 비교
        return product.getProductOptionCombinations().stream()
                .filter(combination -> {
                    try {
                        // JSON 문자열을 파싱하여 Map으로 변환
                        Map<String, Integer> dbOptionIdMap = objectMapper.readValue(
                                combination.getOptionIdCombination(), Map.class);

                        // 파싱된 Map과 사용자가 선택한 옵션 ID Map을 비교
                        return dbOptionIdMap.equals(selectedOptionsIdMap);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .map(ProductOptionCombination::getStock)
                .findFirst();
    }

    // 옵션 조합 찾기
    public ProductOptionCombination getOptionCombinationById(Long id) {
        return productOptionCombinationRepository.findById(id).orElse(null);
    }


    /**
     * 주어진 카테고리 ID에 해당하는 모든 상품을 조회하여 페이지 응답으로 반환하는 함수.
     *
     * @param categoryId 조회할 카테고리의 ID
     * @param pageable   페이징 정보를 포함하는 객체
     * @return           조회된 상품 목록을 페이지 응답 형태로 반환
     *
     * 주어진 카테고리가 3레벨이라면 해당 카테고리 ID만을 사용하여 상품을 조회합니다.
     * 그렇지 않다면, 하위 카테고리를 포함한 모든 카테고리 ID를 수집하여 관련된 상품들을 조회합니다.
     * 수집된 상품은 ProductSummaryResponseDTO로 매핑하여 반환됩니다.
     */
    public PageResponseDTO<ProductSummaryResponseDTO> getProductsByCategoryId(Long categoryId, Pageable pageable) {
        // 주어진 카테고리 ID로 해당 카테고리를 조회, 존재하지 않으면 예외 발생
        Category category = categoryRepository.findWithChildrenById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다."));

        // 조회할 카테고리 ID 목록을 저장할 리스트 생성
        List<Long> categoryIds = new ArrayList<>();
        if (category.getLevel() == 3) {
            // 현재 카테고리가 레벨 3이라면 하위 카테고리가 없으므로 본인의 ID만 추가
            categoryIds.add(category.getId());
        } else {
            // 레벨 3이 아니라면 하위 카테고리의 ID도 함께 수집
            category.getCategoryIds(categoryIds);
        }

        // 수집된 카테고리 ID 목록에 해당하는 상품을 페이징 처리하여 조회
        Page<Product> products = productRepository.findByCategoryIdIn(categoryIds, pageable);

        // 조회한 상품을 ProductSummaryResponseDTO로 변환하고 페이지 응답으로 반환
        return PageResponseDTO.fromPage(products.map(ProductSummaryResponseDTO::fromEntity));
    }
}
