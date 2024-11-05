package com.team1.lotteon.entity;

import com.team1.lotteon.entity.enums.InquiryType1;
import com.team1.lotteon.entity.enums.InquiryType2;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : Article을 상속받는 자주묻는질문 엔티티 생성
*/
@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry extends Article {
    private String type2;
    private String answer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private SellerMember seller;

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
