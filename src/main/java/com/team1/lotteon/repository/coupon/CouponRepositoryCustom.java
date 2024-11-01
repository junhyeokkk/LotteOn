package com.team1.lotteon.repository.coupon;


import com.querydsl.core.Tuple;
import com.team1.lotteon.dto.pageDTO.NewPageRequestDTO;
import com.team1.lotteon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CouponRepositoryCustom {
    Page<Tuple> selectCouponAllForList(NewPageRequestDTO newPageRequestDTO, Pageable pageable);
}
