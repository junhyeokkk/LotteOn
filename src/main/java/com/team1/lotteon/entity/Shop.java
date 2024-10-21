package com.team1.lotteon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private boolean isActive;
}
