package com.team1.lotteon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.lotteon.dto.product.productOption.ModifyRequestProductCombinationDTO;
import com.team1.lotteon.dto.product.productOption.ModifyRequestProductOptionDTO;
import com.team1.lotteon.entity.Cart;
import com.team1.lotteon.entity.OrderItem;
import com.team1.lotteon.entity.Product;
import com.team1.lotteon.entity.enums.CombinationStatus;
import com.team1.lotteon.entity.productOption.OptionCombinationHistory;
import com.team1.lotteon.entity.productOption.OptionItem;
import com.team1.lotteon.entity.productOption.ProductOption;
import com.team1.lotteon.entity.productOption.ProductOptionCombination;
import com.team1.lotteon.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProductOptionService {

    private final OptionRepository productOptionRepo;
    private final ProductOptionCombinationRepository optionCombinationRepo;
    private final OptionCombinationHistoryRepository historyRepo;
    private final ProductRepository productRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OptionItemRepository optionItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;

    // 옵션 및 옵션 조합 업데이트 메서드
    @Transactional
    public void updateProductOptions(Long productId,
                                     List<ModifyRequestProductOptionDTO> options,
                                     List<ModifyRequestProductCombinationDTO> combinations) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        log.info("옵션아이템" + options.toString());
        // 1. 기존 옵션 데이터 조회
        List<ProductOption> existingOptions = productOptionRepo.findByProduct(product);

        log.info("1111" + existingOptions.toString());

        // 2. 옵션 처리
        updateOptions(product, existingOptions, options);


        log.info("2222번까진 오니?");
        // 4. 옵션 조합 처리
        updateOptionCombinations(product, combinations);
        log.info("33333번???");
    }

    // 옵션 업데이트 로직
    private void updateOptions(Product product, List<ProductOption> existingOptions,
                               List<ModifyRequestProductOptionDTO> options) {

        // 2.1 기존 옵션과 비교하여 업데이트 또는 삭제
        Map<String, ProductOption> existingOptionMap = existingOptions.stream()
                .collect(Collectors.toMap(ProductOption::getName, opt -> opt));

        for (ModifyRequestProductOptionDTO optionDTO : options) {
            ProductOption existingOption = existingOptionMap.get(optionDTO.getName());

            if (existingOption == null) {
                log.info("새옵션 상황");
                // 새 옵션 추가
                ProductOption newOption = new ProductOption();
                newOption.setName(optionDTO.getName());
                newOption.setProduct(product);

                List<OptionItem> optionItems = optionDTO.getValues().stream()
                        .map(value -> new OptionItem(value, newOption))
                        .collect(Collectors.toList());

                newOption.setOptionitems(optionItems);
                productOptionRepo.save(newOption);
            } else {
                log.info("업데이트할거없음");
                // 기존 옵션이므로 값 업데이트
                updateOptionItems(existingOption, optionDTO.getValues());
            }
        }

        // 2.2 존재하지 않는 옵션 삭제 및 히스토리 이동
        existingOptions.stream()
                .filter(opt -> options.stream().noneMatch(dto -> dto.getName().equals(opt.getName())))
                .forEach(this::archiveAndDeleteOption);
    }


    // 옵션 값 업데이트 로직
    private void updateOptionItems(ProductOption option, List<String> newValues) {
        // 1. 현재 OptionItem 리스트를 DB에서 조회
        List<OptionItem> existingItems = optionItemRepository.findByProductOption(option);
        Map<Long, OptionItem> existingItemsMap = existingItems.stream()
                .collect(Collectors.toMap(OptionItem::getId, item -> item));

        log.info("현재 존재하는 옵션 아이템들: " + existingItemsMap.toString());
        log.info("새로운 값 리스트: " + newValues.toString());

        Set<String> processedValues = new HashSet<>(); // 업데이트한 값 추적

        // 2. 새 값 추가 및 이름 업데이트
        for (String value : newValues) {
            // 기존에 있는 OptionItem 찾기
            OptionItem existingItem = existingItems.stream()
                    .filter(item -> item.getValue().equals(value))
                    .findFirst()
                    .orElse(null);

            if (existingItem == null) {
                // 기존에 없는 값인 경우, 새로운 OptionItem 생성
                OptionItem newItem = new OptionItem(value, option);
                option.getOptionitems().add(newItem); // 연관관계에 추가
                optionItemRepository.save(newItem); // DB에 저장
                log.info("새로운 아이템 추가: " + newItem.getValue());
            } else {
                // 이미 존재하는 값이므로 이름 업데이트
                processedValues.add(value); // 값 추가
            }
        }

        // 3. 기존에 없는 값 삭제
        for (OptionItem item : existingItems) {
            if (!processedValues.contains(item.getValue())) {
                option.getOptionitems().remove(item); // 연관관계에서 제거
                optionItemRepository.delete(item); // DB에서 삭제
                log.info("삭제된 아이템: " + item.getValue());
            }
        }
    }



    // 옵션 조합 업데이트 로직
    private void updateOptionCombinations(Product product, List<ModifyRequestProductCombinationDTO> combinations) {
        List<ProductOptionCombination> existingCombinations = optionCombinationRepo.findByProduct(product);
        log.info("프로덕트로 조합들은 찾음?" + existingCombinations.toString());

        Map<String, ProductOptionCombination> existingCombMap = existingCombinations.stream()
                .collect(Collectors.toMap(ProductOptionCombination::getOptionValueCombination, comb -> comb));

        log.info("map은 오니? " + existingCombMap.toString());

        List<ProductOptionCombination> newlyAddedCombinations = new ArrayList<>(); // 새롭게 추가된 조합
        Set<String> updatedCombinations = new HashSet<>(); // 업데이트된 조합의 Key 추적용 Set

        for (ModifyRequestProductCombinationDTO combinationDTO : combinations) {

            Map<String, String> optionValueMap = parseCombinationText(combinationDTO.getCombinationText());


            String optionValueCombinationJson = convertToJson(optionValueMap); // 값 조합 JSON 생성
            String optionIdCombinationJson = generateOptionIdCombination(optionValueMap); // ID 조합 JSON 생성

            ProductOptionCombination existingComb = existingCombMap.get(optionValueCombinationJson);

            log.info("키조합 파싱" + optionIdCombinationJson);
            log.info("조합 파싱" + optionValueCombinationJson);
//            log.info("콤비 디티오" + existingComb.toString());

            int stock = Math.max(combinationDTO.getStock(), 0);  // 재고 수량을 0 이상으로 설정
            CombinationStatus status;

            try {
                status = CombinationStatus.fromString(combinationDTO.getStatus());
            } catch (IllegalArgumentException e) {
                log.error("Invalid combination status: " + combinationDTO.getStatus(), e);
                continue;
            }


            if (existingComb == null) {
                log.info("새 조합 추가 중...");
                ProductOptionCombination newComb = new ProductOptionCombination();
                newComb.setProduct(product);
                newComb.setOptionValueCombination(optionValueCombinationJson);
                newComb.setOptionIdCombination(optionIdCombinationJson);
                newComb.setStock(combinationDTO.getStock());
                newComb.setCombinationStatus(status);
                log.info("new combination: " + newComb.toString());
                newlyAddedCombinations.add(newComb); // 새롭게 추가된 조합 저장
                optionCombinationRepo.save(newComb);

                log.info("저장????");
            } else {
                log.info("기존 조합 업데이트 중...");
                existingComb.setStock(combinationDTO.getStock());
                existingComb.setCombinationStatus(CombinationStatus.valueOf(combinationDTO.getStatus()));
                existingComb.setOptionValueCombination(optionValueCombinationJson);
                existingComb.setOptionIdCombination(optionIdCombinationJson);
                optionCombinationRepo.save(existingComb);

                // 업데이트된 조합 Key 저장
                updatedCombinations.add(optionValueCombinationJson);
            }
        }


        // 아카이브 처리: 기존 조합 중 업데이트되지 않은 조합들만 비활성화
        List<ProductOptionCombination> removedCombinations = existingCombinations.stream()
                .filter(comb -> !updatedCombinations.contains(comb.getOptionValueCombination()))
                .collect(Collectors.toList());

        removedCombinations.forEach(this::archiveAndDisableCombination);

        // 로그 확인
        log.info("새로 추가된 조합들: " + newlyAddedCombinations);
        log.info("삭제된 조합들: " + removedCombinations);
    }

    // 옵션 삭제 및 히스토리 아카이브
    private void archiveAndDeleteOption(ProductOption option) {
        // 관련 ProductOptionCombination 조회
        List<ProductOptionCombination> combinations = optionCombinationRepo.findByProduct(option.getProduct());

        combinations.forEach(combination -> {
            OptionCombinationHistory history = new OptionCombinationHistory();
            history.setCombinationSnapshot(combination.getOptionValueCombination());

            // JSON 필드에서 값을 읽어와서 히스토리에 저장
            history.setOptionCombinationValues(combination.getOptionValueCombination());
            history.setCreatedAt(LocalDateTime.now());
            history.setVersion(combination.getVersion());
            history.setActive(false); // 비활성화 표시
            historyRepo.save(history); // 히스토리에 저장

        });

        // 옵션 삭제
        productOptionRepo.delete(option);
    }

    // 조합 삭제 및 히스토리 아카이브
    private void archiveAndDisableCombination(ProductOptionCombination combination) {
        log.info("다들어오니?" + combination.toString());

        // OrderItem과 Cart에서 해당 조합을 참조하고 있는지 확인
        List<OrderItem> orderItems = orderItemRepository.findByProductOptionCombination(combination);
        List<Cart> carts = cartRepository.findByProductOptionCombination(combination);

        if (orderItems.isEmpty() && carts.isEmpty()) {
            // OrderItem과 Cart가 참조하지 않는 경우 조합을 안전하게 삭제
            log.info("조합을 참조하는 항목이 없어 삭제합니다.");
            safelyDeleteCombination(combination);  // 안전하게 삭제
        } else {
            // 참조 중인 항목이 있어도 스냅샷을 보존하고 참조를 해제 후 삭제
            log.info("참조 중인 조합이 있지만 스냅샷을 보존하고 삭제를 진행합니다.");
            safelyDeleteCombination(combination);  // 안전하게 삭제 처리
        }
    }


    // Map을 JSON 형식으로 변환하는 메서드 (값이 문자열인 경우)
    private String convertToJson(Map<String, String> map) {
        return map.entrySet().stream()
                .map(entry -> "\"" + entry.getKey() + "\": \"" + entry.getValue() + "\"") // 값이 문자열 형태로 추가
                .collect(Collectors.joining(", ", "{", "}"));
    }

    // combinationText를 파싱하여 옵션명-값의 Map을 생성하는 메서드
    private Map<String, String> parseCombinationText(String combinationText) {
        return Arrays.stream(combinationText.split(","))
                .map(String::trim)
                .map(entry -> entry.split(":"))
                .collect(Collectors.toMap(
                        entry -> entry[0].trim(),
                        entry -> entry[1].trim()
                ));
    }

    // optionValueMap을 바탕으로 각 옵션 값의 ID 조합 JSON을 생성하는 메서드
    private String generateOptionIdCombination(Map<String, String> optionValueMap) {
        Map<String, Long> optionIdMap = new HashMap<>();

        optionValueMap.forEach((optionName, optionValue) -> {
            OptionItem optionItem = optionItemRepository.findByProductOptionNameAndValue(optionName, optionValue)
                    .orElseThrow(() -> new IllegalArgumentException("OptionItem not found for option: " + optionName + ", value: " + optionValue));
            optionIdMap.put(optionName, optionItem.getId());
        });

        // 직접 JSON 형식을 구성하여 띄어쓰기 포함
        return optionIdMap.entrySet().stream()
                .map(entry -> "\"" + entry.getKey() + "\": " + entry.getValue()) // 콜론 뒤에 띄어쓰기 추가
                .collect(Collectors.joining(", ", "{", "}"));
    }


    // combinationText 데이터를 Map<String, String>으로 변환 후 JSON 문자열로 만들어 비교
    private String convertCombinationTextToJson(String combinationText) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        // "key: value, key: value" 형식을 Map으로 변환
        Map<String, String> combinationMap = Arrays.stream(combinationText.split(","))
                .map(String::trim)
                .map(String::trim)
                .map(part -> part.split(":"))
                .collect(Collectors.toMap(
                        part -> part[0],
                        part -> part[1]
                ));

        // Map을 JSON 문자열로 변환
        return objectMapper.writeValueAsString(combinationMap);
    }

    // 조합 삭제할 경우!!!

    // 조합을 삭제하기 전에 OrderItem과 Cart의 스냅샷을 보존
    private void ensureCombinationSnapshotInOrderItems(ProductOptionCombination combination) {
        List<OrderItem> orderItems = orderItemRepository.findByProductOptionCombination(combination);
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getOptionCombinationSnapshot() == null || orderItem.getOptionCombinationSnapshot().isEmpty()) {
                orderItem.setOptionCombinationSnapshot(combination.getOptionValueCombination());
                orderItemRepository.save(orderItem);  // 스냅샷 저장
            }
        }
    }

    private void detachCombinationFromCarts(ProductOptionCombination combination) {
        List<Cart> carts = cartRepository.findByProductOptionCombination(combination);
        for (Cart cart : carts) {
            cart.setProductOptionCombination(null);  // 참조 해제
            cartRepository.save(cart);  // 변경사항 저장
//            notifyUserForCombinationRemoval(cart.getMember(), combination);  // 사용자에게 알림 (선택 사항)
        }
    }

    private void archiveCombination(ProductOptionCombination combination) {
        OptionCombinationHistory history = new OptionCombinationHistory();
        history.setCombinationSnapshot(combination.getOptionValueCombination());
        history.setOptionCombinationValues(combination.getOptionValueCombination());
        history.setVersion(combination.getVersion());
        history.setCreatedAt(LocalDateTime.now());
        history.setActive(false);
        historyRepo.save(history);
    }

    private void deleteCombination(ProductOptionCombination combination) {
        optionCombinationRepo.delete(combination);
        log.info("삭제된 조합: " + combination.getOptionValueCombination());
    }

    public void safelyDeleteCombination(ProductOptionCombination combination) {
        ensureCombinationSnapshotInOrderItems(combination);
        detachCombinationFromCarts(combination);
        archiveCombination(combination);
        deleteCombination(combination);
    }

}