package com.team1.lotteon.service;

import com.team1.lotteon.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class ShopService {
    private final ShopRepository shopRepository;
    //회사명 존재 여부 확인
    public boolean isshopnameExist(String shopname) {
        return shopRepository.existsByShopName(shopname);
    }
    // 사업자 등록번호 존재 여부 확인
    public boolean isBusinessRegistrationExist(String businessRegistration) {
        return shopRepository.existsByBusinessRegistration(businessRegistration);
    }
    // 통신판매업번호 존재 여부 확인
//    public boolean isECommerceRegistrationExist(String eCommerceRegistration) {
//        return shopRepository.existsByECommerceRegistration(eCommerceRegistration);
//    }
}
