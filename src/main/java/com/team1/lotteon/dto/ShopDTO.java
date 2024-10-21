package com.team1.lotteon.dto;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopDTO {
    private Long id;
    private boolean isActive;
    private String addr1;
    private String addr2;
    private String businessRegistration;
    private String eCommerceRegistration;
    private String fax;
    private String memberId;
    private String ph;
    private String representative;
    private String shopName;
    private String zip;
}
