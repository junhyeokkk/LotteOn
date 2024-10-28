package com.team1.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;


/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 상점 엔티티 생성
*/
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Shop {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String shopName;
    private String representative;
    private String businessRegistration;
    private String eCommerceRegistration;
    private String ph;
    private String fax;
    @Embedded
    private Address address;
    private int isActive;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "shop")
    private SellerMember sellerMember;

}
