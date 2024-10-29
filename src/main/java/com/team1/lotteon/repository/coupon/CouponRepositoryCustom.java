package com.team1.lotteon.repository.coupon;


import com.querydsl.core.Tuple;
import com.team1.lotteon.dto.pageDTO.NewPageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponRepositoryCustom {
    Page<Tuple> selectCouponAllForList(NewPageRequestDTO newPageRequestDTO, Pageable pageable);
}
