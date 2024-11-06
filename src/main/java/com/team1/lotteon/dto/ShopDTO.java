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
    private int isActive;
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
    //2024/11/06 이도영 이메일 추가
    private String email;
    //상점 갯수 출력을 위한 번호
    private int shopNumber;
}
