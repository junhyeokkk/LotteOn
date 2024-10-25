package com.team1.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : Member를 상속받는 판매자 엔티티 생성
*/
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DiscriminatorValue("Seller")
public class SellerMember extends Member {
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "shop_id")
    private Shop shop;
}
