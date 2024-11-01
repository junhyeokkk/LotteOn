package com.team1.lotteon.service.admin;

import com.team1.lotteon.dto.PointDTO;
import com.team1.lotteon.dto.order.OrderDTO;
import com.team1.lotteon.dto.order.OrderPageRequestDTO;
import com.team1.lotteon.dto.order.OrderPageResponseDTO;
import com.team1.lotteon.dto.point.PointPageRequestDTO;
import com.team1.lotteon.dto.point.PointPageResponseDTO;
import com.team1.lotteon.entity.Order;
import com.team1.lotteon.entity.Point;
import com.team1.lotteon.entity.SellerMember;
import com.team1.lotteon.repository.OrderRepository;
import com.team1.lotteon.security.MyUserDetails;
import com.team1.lotteon.util.MemberUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/*
    날짜 : 2024/11/1
    이름 : 최준혁
    내용 : AdminOrder 기능구현을 위한 Service 작성
*/

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    // Order select 페이징 (ADMIN) + 검색기능 // seller일때, admin일때 구분
    public OrderPageResponseDTO getOrders(OrderPageRequestDTO orderPageRequestDTO, SellerMember member) {
        // Pageable 생성
        Pageable pageable = orderPageRequestDTO.getPageable("id");

        // 주문 데이터 가져오기
        Page<Order> orderPage;


        if (member != null) {
            // SellerMember일 경우, 자신의 shopId와 일치하는 주문만 가져오도록 shopId 추가 필터링
            Long shopId = member.getShop().getId();

            if ("all".equals(orderPageRequestDTO.getType())) {
                // 모든 주문을 조회하면서 shopId 조건 추가
                orderPage = orderRepository.findByShopId(shopId, pageable);
            } else if (orderPageRequestDTO.getType() != null && !orderPageRequestDTO.getType().isEmpty() &&
                    (orderPageRequestDTO.getKeyword() != null && !orderPageRequestDTO.getKeyword().isEmpty())) {
                // 조건에 맞춰 동적 쿼리 실행 + shopId로 필터링
                orderPage = orderRepository.findByDynamicTypeWithShopId(orderPageRequestDTO.getKeyword(), orderPageRequestDTO.getType(), pageable, shopId);
            } else {
                // 기본 조건이 없으면 shopId로 필터링하여 모든 주문 조회
                orderPage = orderRepository.findByShopId(shopId, pageable);
            }
        } else {
            // Admin일 때, type과 keyword로 모든 데이터 가져오기
            if ("all".equals(orderPageRequestDTO.getType())) {
                orderPage = orderRepository.findAll(pageable);
            } else if (orderPageRequestDTO.getType() != null && !orderPageRequestDTO.getType().isEmpty() &&
                    (orderPageRequestDTO.getKeyword() != null && !orderPageRequestDTO.getKeyword().isEmpty())) {
                // QueryDSL을 사용하여 동적 쿼리 실행
                orderPage = orderRepository.findByDynamicType(orderPageRequestDTO.getKeyword(), orderPageRequestDTO.getType(), pageable);
            } else {
                // 조건이 맞지 않을 경우 모든 데이터를 가져옴
                orderPage = orderRepository.findAll(pageable);
            }
        }

        // Order 엔티티를 DTO로 변환
        List<OrderDTO> dtoList = orderPage.getContent().stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());

        // OrderPageResponseDTO 생성 및 반환
        return new OrderPageResponseDTO(orderPageRequestDTO, dtoList, (int) orderPage.getTotalElements());
    }

}
