package com.team1.lotteon.repository.custom;

import com.team1.lotteon.dto.RequestDTO.BannerPageRequestDTO;
import com.team1.lotteon.entity.Banner;
import groovy.lang.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepositoryCustom {
    public Page<Tuple> selectBannerAllForList(BannerPageRequestDTO pagerequestDTO, Pageable pageable, int cateNo);
}
