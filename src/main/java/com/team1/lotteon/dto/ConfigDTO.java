package com.team1.lotteon.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/*
    날짜 : 2024/10/23
    이름 : 최준혁
    내용 : Config DTO 생성 (회사정보)
*/

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDTO {

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
