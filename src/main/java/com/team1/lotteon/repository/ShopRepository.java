package com.team1.lotteon.repository;

import com.team1.lotteon.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    boolean existsByShopName(String shopname);
    boolean existsByBusinessRegistration(String businessRegistration);
//    boolean existsByECommerceRegistration(String eCommerceRegistration);
}
