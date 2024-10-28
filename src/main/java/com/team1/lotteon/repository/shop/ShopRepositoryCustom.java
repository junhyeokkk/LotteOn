package com.team1.lotteon.repository.shop;

import com.querydsl.core.Tuple;
import com.team1.lotteon.dto.pageDTO.NewPageRequestDTO;
import com.team1.lotteon.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShopRepositoryCustom {
    public Shop selectShop(int shop);
    public Page<Tuple> selectShopAllForList(NewPageRequestDTO newPageRequestDTO, Pageable pageable);
}
