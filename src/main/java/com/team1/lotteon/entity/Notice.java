package com.team1.lotteon.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : Article을 상속받는 공지사항 엔티티 생성
*/
@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
public class Notice extends Article {
}
