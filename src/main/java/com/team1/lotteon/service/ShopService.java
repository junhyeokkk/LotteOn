package com.team1.lotteon.service;
/*
     날짜 : 2024/10/21
     이름 : 이도영
     내용 : 상점 관련 서비스

     수정이력
      - 2024/10/25 이도영 - 유효성검사
      - 2024/10/28 이도영 - 관리자 상점 페이지 출력
      - 2024/11/06 이도영 - 이메일 존재여부,판매자 아이디 검색,판매자 비밀번호 검색 기능 추가
      - 2024/11/08 이도영 - 통신판매업번호,전화번호,팩스번호 존재 여부
*/
import com.querydsl.core.Tuple;
import com.team1.lotteon.dto.ShopDTO;
import com.team1.lotteon.dto.pageDTO.NewPageRequestDTO;
import com.team1.lotteon.dto.pageDTO.NewPageResponseDTO;
import com.team1.lotteon.entity.SellerMember;
import com.team1.lotteon.entity.Shop;
import com.team1.lotteon.repository.Memberrepository.SellerMemberRepository;
import com.team1.lotteon.repository.shop.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class ShopService {
    private final ShopRepository shopRepository;
    private final ModelMapper modelMapper;
    private final SellerMemberRepository sellerMemberRepository;
    // 기본 상점 화면 출력
    public NewPageResponseDTO<ShopDTO> selectshopAll(NewPageRequestDTO newPageRequestDTO) {
        // 페이징 정보를 설정합니다.
        Pageable pageable = newPageRequestDTO.getPageable("id", false);

        Page<Shop> shopPage;

        // 검색 유형
        String type = newPageRequestDTO.getType();
        // 검색 키워드
        String keyword = newPageRequestDTO.getKeyword();

        // 조건에 따른 필터링 적용
        if (type != null && keyword != null && !keyword.isEmpty()) {
            // 검색 유형에 따라 필터
            switch (type) {
                // 상호명으로 검색
                case "shopName":
                    shopPage = shopRepository.findByShopNameContaining(keyword, pageable);
                    break;
                // 대표자명으로 검색
                case "representative":
                    shopPage = shopRepository.findByRepresentativeContaining(keyword, pageable);
                    break;
                // 사업자 등록번호로 검색
                case "businessRegistration":
                    shopPage = shopRepository.findByBusinessRegistrationContaining(keyword, pageable);
                    break;
                // 연락처로 검색
                case "contact":
                    shopPage = shopRepository.findByPhContaining(keyword, pageable);
                    break;
                default:
                    // 조건이 없을 경우 전체 검색
                    shopPage = shopRepository.findAll(pageable);
                    break;
            }
        } else {
            // 조건이 없을 경우 전체 검색
            shopPage = shopRepository.findAll(pageable);
        }

        // 순서 번호 계산 (페이지 시작 번호)
        AtomicInteger startNumber = new AtomicInteger((newPageRequestDTO.getPg() - 1) * newPageRequestDTO.getSize() + 1);

        // Shop 엔티티를 ShopDTO로 변환하고 순서 번호 설정
        List<ShopDTO> shopList = shopPage.getContent().stream()
                .map(shop -> {
                    ShopDTO shopDTO = modelMapper.map(shop, ShopDTO.class);
                    shopDTO.setShopNumber(startNumber.getAndIncrement()); // 순서 번호 설정
                    return shopDTO;
                })
                .collect(Collectors.toList());

        // 결과를 NewPageResponseDTO에 담아 반환
        return NewPageResponseDTO.<ShopDTO>builder()
                .newPageRequestDTO(newPageRequestDTO) // 요청 정보 설정
                .dtoList(shopList) // DTO 리스트 설정
                .total((int) shopPage.getTotalElements()) // 총 요소 수 설정
                .build();
    }


    //회사명 존재 여부 확인
    public boolean isshopnameExist(String shopname) {
        return shopRepository.existsByShopName(shopname);
    }
    // 사업자 등록번호 존재 여부 확인
    public boolean isBusinessRegistrationExist(String businessRegistration) {
        return shopRepository.existsByBusinessRegistration(businessRegistration);
    }

    public void deleteShops(List<Long> ids) {
        shopRepository.deleteAllById(ids);
    }

    //이메일 존재여부
    public boolean isShopEmailExist(String email) {
        return shopRepository.existsByEmail(email);
    }

    //판매자 아이디 검색
    public Shop findUserIdByNameAndEmail(String name, String email) {
        return shopRepository.findByRepresentativeAndEmail(name, email);
    }
    //판매자 비밀번호 검색
    public Optional<Shop> getShopByUidAndEmail(String uid, String email) {
        return sellerMemberRepository.findByUidAndShop_Email(uid, email)
                .map(SellerMember::getShop);
    }
    // 통신판매업번호 존재 여부 확인
    public boolean isECommerceRegistrationExist(String value) {
        return shopRepository.existsByECommerceRegistration(value);
    }
    //전화번호 존재 여부 확인
    public boolean isphExist(String ph) {
        return shopRepository.existsByph(ph);
    }
    //팩스번호 존재 여부 확인
    public boolean isFaxExist(String fax) {
        return shopRepository.existsByFax(fax);
    }
}
