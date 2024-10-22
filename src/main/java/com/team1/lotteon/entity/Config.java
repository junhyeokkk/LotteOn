package com.team1.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int companyid;

    // 사이트
    private String title;
    private String sub_title;

    // 로고
    private String headerlogo;
    private String footerlogo;
    private String favicon;

    // 기업정보
    private String b_name;
    private String ceo;
    private String b_num;
    private String b_report;
    private String addr1;
    private String addr2;

   // 고객센터
   private String cs_num;
   private String cs_time;
   private String cs_email;
   private String dispute;

   // 카피라이트
   private String copyright;

}
