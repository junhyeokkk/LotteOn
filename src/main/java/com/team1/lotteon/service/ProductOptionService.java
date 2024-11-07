package com.team1.lotteon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team1.lotteon.dto.product.productOption.ModifyRequestProductCombinationDTO;
import com.team1.lotteon.dto.product.productOption.ModifyRequestProductOptionDTO;
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

    // 옵션 및 옵션 조합 업데이트 메서드
    @Transactional
    public void updateProductOptions(Long productId,
                                     List<ModifyRequestProductOptionDTO> options,
                                     List<ModifyRequestProductCombinationDTO> combinations) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        // 1. 기존 옵션 데이터 조회
        List<ProductOption> existingOptions = productOptionRepo.findByProduct(product);

        log.info("1111" + existingOptions.toString());

        // 2. 옵션 처리
        updateOptions(product, existingOptions, options);

        log.info("2222번까진 오니?");
        // 3. 옵션 조합 처리
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
        Map<String, OptionItem> existingItems = option.getOptionitems().stream()
                .collect(Collectors.toMap(OptionItem::getValue, item -> item));

        // 새 값 추가
        for (String value : newValues) {
            if (!existingItems.containsKey(value)) {
                OptionItem newItem = new OptionItem(value, option);
                option.getOptionitems().add(newItem);
            }
        }

        // 기존에 없는 값 삭제
        option.getOptionitems().removeIf(item -> !newValues.contains(item.getValue()));
    }

    // 옵션 조합 업데이트 로직
    private void updateOptionCombinations(Product product, List<ModifyRequestProductCombinationDTO> combinations) {
        List<ProductOptionCombination> existingCombinations = optionCombinationRepo.findByProduct(product);
        log.info("프로덕트로 조합들은 찾음?" + existingCombinations.toString());

        Map<String, ProductOptionCombination> existingCombMap = existingCombinations.stream()
                .collect(Collectors.toMap(ProductOptionCombination::getOptionValueCombination, comb -> comb));

        log.info("map은 오니? " + existingCombMap.toString());

        for (ModifyRequestProductCombinationDTO combinationDTO : combinations) {
            ProductOptionCombination existingComb = existingCombMap.get(combinationDTO.getCombinationText());
            Map<String, String> optionValueMap = parseCombinationText(combinationDTO.getCombinationText());

            String optionValueCombinationJson = convertToJson(optionValueMap); // 값 조합 JSON 생성
            String optionIdCombinationJson = generateOptionIdCombination(optionValueMap); // ID 조합 JSON 생성

            log.info("키조합 파싱" + optionIdCombinationJson);
            log.info("조합 파싱" + optionValueCombinationJson);
            log.info("콤비 디티오" + combinationDTO.toString());

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
                optionCombinationRepo.save(newComb);
                log.info("저장????");
            } else {
                log.info("기존 조합 업데이트 중...");
                existingComb.setStock(combinationDTO.getStock());
                existingComb.setCombinationStatus(CombinationStatus.valueOf(combinationDTO.getStatus()));
                existingComb.setOptionValueCombination(optionValueCombinationJson);
                existingComb.setOptionIdCombination(optionIdCombinationJson);
                optionCombinationRepo.save(existingComb);
            }
        }

        // 전달된 조합에 없는 기존 조합들을 히스토리에 저장하고 비활성화
        existingCombinations.stream()
                .filter(comb -> combinations.stream()
                        .noneMatch(dto -> {
                            try {
                                return convertCombinationTextToJson(dto.getCombinationText())
                                        .equals(comb.getOptionValueCombination());
                            } catch (JsonProcessingException e) {
                                log.error("Error during filtering", e);
                                return false;
                            }
                        }))
                .forEach(this::archiveAndDisableCombination);
    }

    // 옵션 삭제 및 히스토리 아카이브
    private void archiveAndDeleteOption(ProductOption option) {
        // 관련 ProductOptionCombination 조회
        List<ProductOptionCombination> combinations = optionCombinationRepo.findByProduct(option.getProduct());

        combinations.forEach(combination -> {
            OptionCombinationHistory history = new OptionCombinationHistory();
            history.setProductOptionCombination(combination);

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
        OptionCombinationHistory history = new OptionCombinationHistory();
        history.setProductOptionCombination(combination);
        history.setOptionCombinationValues(combination.getOptionValueCombination());
        history.setVersion(combination.getVersion());
        history.setCreatedAt(LocalDateTime.now());
        history.setActive(false);  // 기록을 비활성화 상태로 저장
        historyRepo.save(history); // 히스토리에 저장

        // 기존 조합 비활성화
        combination.setCombinationStatus(CombinationStatus.STOP); // 조합 상태를 "판매중지" 또는 비활성화
        optionCombinationRepo.save(combination);
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

        try {
            return new ObjectMapper().writeValueAsString(optionIdMap); // ID 조합 JSON 반환
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error generating optionIdCombination JSON", e);
        }
    }
    // optionValueMap을 바탕으로 값 조합 JSON을 생성하는 메서드
    private String convertToJson(Map<String, String> map) {
        try {
            return new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting map to JSON", e);
        }
    }
    // combinationText 데이터를 Map<String, String>으로 변환 후 JSON 문자열로 만들어 비교
    private String convertCombinationTextToJson(String combinationText) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        // "key: value, key: value" 형식을 Map으로 변환
        Map<String, String> combinationMap = Arrays.stream(combinationText.split(","))
                .map(String::trim)
                .map(part -> part.split(":"))
                .collect(Collectors.toMap(
                        part -> part[0].trim(),
                        part -> part[1].trim()
                ));

        // Map을 JSON 문자열로 변환
        return objectMapper.writeValueAsString(combinationMap);
    }
}