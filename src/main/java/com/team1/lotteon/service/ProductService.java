package com.team1.lotteon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.lotteon.dto.PageResponseDTO;
import com.team1.lotteon.dto.product.*;
import com.team1.lotteon.dto.product.productOption.*;
import com.team1.lotteon.entity.*;
import com.team1.lotteon.entity.enums.CombinationStatus;
import com.team1.lotteon.entity.productOption.ProductOption;
import com.team1.lotteon.entity.productOption.OptionItem;
import com.team1.lotteon.entity.productOption.ProductOption;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import com.team1.lotteon.repository.*;
import com.team1.lotteon.repository.review.ReviewRepository;
import com.team1.lotteon.repository.shop.ShopRepository;
import com.team1.lotteon.util.MemberUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
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
@Transactional
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
    private final ShopRepository shopRepository;
    private final ProductOptionService productOptionService;
    private final ReviewRepository reviewRepository;
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

        //2024/11/13 이도영 상품 등록시 상점 활성화 여부 변경
        if(seller.getShop().getIsActive()==0){
            Shop shop = seller.getShop();
            shop.setIsActive(1);
            shopRepository.save(shop);
        }

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
                        .combinationStatus(CombinationStatus.SALE)
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
        return PageResponseDTO.fromPage(products.map(product -> {
            double score = getRoundedAverageScore(product.getId());
            long reviewCount = reviewRepository.countByProductId(product.getId());
            return ProductSummaryResponseDTO.fromEntity(product, score, reviewCount);
        } ));
    }

    public PageResponseDTO<ProductSummaryResponseDTO> searchProducts(ProductSearchRequestDto productSearchRequestDto) {
        Category category = null;

        if (productSearchRequestDto.getCategoryId() != null) {
            category = categoryRepository.findById(productSearchRequestDto.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        }
        List<Long> categoryIds = new ArrayList<>();
        if (category != null) {
            category.getCategoryIds(categoryIds);
        }

//        if (category != null && category.getLevel() == 3) {
//            categoryIds.add(category.getId());
//        }

        Pageable pageable = productSearchRequestDto.toPageable();
        Page<Product> products = productRepository.searchProducts(productSearchRequestDto, categoryIds, pageable);
        return PageResponseDTO.fromPage(products.map(product -> {
            double score = getRoundedAverageScore(product.getId());
            long reviewCount = reviewRepository.countByProductId(product.getId());
            return ProductSummaryResponseDTO.fromEntity(product, score, reviewCount);
        } ));
    }

    public ProductDTO getProductById(Long id) {

        log.info("서비스 입성");
        Product product = productRepository.findById(id).orElse(null);
        //2024/11/08 이도영 상품 조회수 증가
        if(product != null){
            product.setViews(product.getViews()+1);
            productRepository.save(product);
        }
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
     * @return 조회된 상품 목록을 페이지 응답 형태로 반환
     * <p>
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
        return PageResponseDTO.fromPage(products.map(product -> {
            double score = getRoundedAverageScore(product.getId());
            long reviewCount = reviewRepository.countByProductId(product.getId());
            return ProductSummaryResponseDTO.fromEntity(product, score, reviewCount);
        } ));
    }

    //도영 2024/11/03 상품 아이디를 활용해서 상점 아이디 검색 기능
    public Long getShopIdByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        // Product가 존재하면 해당 Product의 shop 객체의 id를 반환
        return product.getShop() != null ? product.getShop().getId() : null;
    }



    // 상품 수정 저장
    public void updateProduct(Long id, ProductDTO productDTO,
                              List<ModifyRequestProductOptionDTO> options,
                              List<ModifyRequestProductCombinationDTO> combinations,
                              MultipartFile productImg1, MultipartFile productImg2, MultipartFile productImg3) throws IOException {
        // 1. 상품 조회 및 검증
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 상품을 찾을 수 없습니다. ID: " + id));

        // 2. 전달받은 productDTO의 데이터로 Product 필드 업데이트
        product.setProductName(productDTO.getProductName());
        product.setDescription(productDTO.getDescription());
        product.setManufacturer(productDTO.getManufacturer());
        product.setPrice(productDTO.getPrice());
        product.setDiscountRate(productDTO.getDiscountRate());
        product.setPoint(productDTO.getPoint());
        product.setStock(productDTO.getStock());
        product.setDeliveryFee(productDTO.getDeliveryFee());
        product.setStatus(productDTO.getStatus());
        product.setWarranty(productDTO.getWarranty());
        product.setReceiptIssued(productDTO.getReceiptIssued());
        product.setBusinessType(productDTO.getBusinessType());
        product.setOrigin(productDTO.getOrigin());

        // 3. 이미지 파일 처리 및 기존 파일 삭제 후 저장 경로 업데이트
        if (productImg1 != null && !productImg1.isEmpty()) {
            deleteFile(product.getProductImg1()); // 기존 파일 삭제
            String filePath1 = saveFile(productImg1);
            product.setProductImg1(filePath1);
        }

        if (productImg2 != null && !productImg2.isEmpty()) {
            deleteFile(product.getProductImg2()); // 기존 파일 삭제
            String filePath2 = saveFile(productImg2);
            product.setProductImg2(filePath2);
        }

        if (productImg3 != null && !productImg3.isEmpty()) {
            deleteFile(product.getProductImg3()); // 기존 파일 삭제
            String filePath3 = saveFile(productImg3);
            product.setProductImg3(filePath3);
        }

//        // 4. 옵션 및 옵션 조합 데이터 업데이트
        productOptionService.updateProductOptions(product.getId(), options, combinations);

        log.info("나오니:????");
        // 5. 업데이트된 Product 엔티티 저장
        productRepository.save(product);
    }

    // 파일 저장 메서드
    public String saveFile(MultipartFile file) throws IOException {
        String UpdateUploadDir = uploadDir + "/product";

        // 파일의 원래 이름에서 확장자를 추출
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // UUID를 사용해 랜덤 파일명 생성
        String randomFileName = UUID.randomUUID().toString() + extension;

        Path filePath = Paths.get(UpdateUploadDir, randomFileName);
        Files.createDirectories(filePath.getParent()); // 경로가 없으면 생성
        Files.write(filePath, file.getBytes());

        return filePath.toString(); // 저장된 파일의 전체 경로 반환
    }

    // 파일 삭제 메서드 (새로운 이미지 교체이므로 저장공간 확보)
    private void deleteFile(String filePath) {
        if (filePath != null) {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    //히트 상품 조회수 가장 많은 순 8개
    public List<Product> getMainproductsBybesthit() {
        return productRepository.findTop8ByOrderByViewsDesc();
    }
    //추천 상품 리뷰가 가장 많으면서 평점이 높은 순서대로 8개
    public List<Product> getTopProductsByReviewCountAndScore() {
        return productRepository.findTopProductsByReviewCountAndScore();
    }
    //최신상품 가장 최근에 등록된 상품 순서 대로 8개
    public List<Product> getLatestProducts() {
        return productRepository.findTop8ByOrderByCreatedAtDesc();
    }
    //인기 상품 구매율이 가장 높으면서 평점이 높은순
    public List<Product> getTopSellingAndRatedProducts() {
        return productRepository.findTopSellingAndRatedProducts();
    }

    public List<Product> getTopDiscountedProducts() {
        return productRepository.findTop8ByOrderByDiscountRateDesc(); // 또는 findTop10ByOrderByDiscountRateDesc() 호출
    }

    public double getRoundedAverageScore(Long productId) {
        double averageScore = reviewRepository.findAverageScoreByProductId(productId);
        BigDecimal roundedScore = BigDecimal.valueOf(averageScore).setScale(1, RoundingMode.HALF_UP);
        return roundedScore.doubleValue();
    }
}
