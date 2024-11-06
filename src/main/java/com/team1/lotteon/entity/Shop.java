package com.team1.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;


/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 상점 엔티티 생성
    수정사항
        - 2024/11/06 이도영 판매자 이메일 추가
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
    private String email;
    @Embedded
    private Address address;
    private int isActive;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "shop")
    private SellerMember sellerMember;

}
