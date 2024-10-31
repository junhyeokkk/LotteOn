package com.team1.lotteon.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CarttoOrderRequestDTO {
    private List<Long> selectedCartIds;  // 선택된 장바구니 항목의 ID 목록
}