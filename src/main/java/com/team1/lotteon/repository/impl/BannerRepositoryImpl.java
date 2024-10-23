package com.team1.lotteon.repository.impl;

import com.team1.lotteon.dto.RequestDTO.BannerPageRequestDTO;
import com.team1.lotteon.entity.Banner;
import com.team1.lotteon.repository.BannerRepository;
import com.team1.lotteon.repository.custom.BannerRepositoryCustom;
import groovy.lang.Tuple;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Log4j2
@AllArgsConstructor
@Repository
public class BannerRepositoryImpl implements BannerRepositoryCustom {


    @Override
    public Page<Tuple> selectBannerAllForList(BannerPageRequestDTO pagerequestDTO, Pageable pageable, int id) {
        return null;
    }
}
