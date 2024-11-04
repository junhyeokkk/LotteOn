package com.team1.lotteon.entity;

import com.team1.lotteon.entity.enums.FaqType1;
import com.team1.lotteon.entity.enums.FaqType2;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : FAQ 엔티티 생성

    수정내용
    2024/11/03 - 김소희 Setter 수동 정의
*/
@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FAQ extends Article {
    private String type2;

    // type2 필드에 대한 세터 메서드 수동 정의
    public void setType2(String type2) {
        this.type2 = type2;
    }
}
