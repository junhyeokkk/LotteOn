package com.team1.lotteon.service.admin;

import com.team1.lotteon.dto.order.DeliveryDTO;
import com.team1.lotteon.entity.SellerMember;
import com.team1.lotteon.entity.enums.DeliveryStatus;
import com.team1.lotteon.repository.DeliveryRepoistory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/*
    날짜 : 2024/11/4
    이름 : 최준혁
    내용 : AdminDelivery 기능구현을 위한 Service 작성
*/
@RequiredArgsConstructor
@Service
public class AdminDeliveryService {

    private final DeliveryRepoistory deliveryRepository;
    private final ModelMapper modelMapper;

    // 권한에 따라 배송 목록을 페이징하여 조회
    public Page<DeliveryDTO> getDeliveryListByUserRole(Object loggedInUser, Pageable pageable) {
        DeliveryStatus status = DeliveryStatus.READY;

        if (loggedInUser instanceof SellerMember seller) {
            Long shopId = seller.getShop().getId();
            return deliveryRepository.findByStatusNotAndOrderItem_Product_Shop_Id(status, shopId, pageable)
                    .map(delivery -> modelMapper.map(delivery, DeliveryDTO.class));
        } else {
            return deliveryRepository.findByStatusNot(status, pageable)
                    .map(delivery -> modelMapper.map(delivery, DeliveryDTO.class));
        }
    }
}
