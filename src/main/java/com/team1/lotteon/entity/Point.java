package com.team1.lotteon.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 포인트 엔티티 생성
*/
@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "point")
public class Point{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;    // 지급내용

    private int givePoints;  // 지급 포인트
    private int acPoints;   // 잔여 포인트

    @CreationTimestamp
    private LocalDateTime createdat; // 지급날짜
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private GeneralMember member;

    public void changeMember(GeneralMember member){
        this.member = member;
    }

    public void setGivePoints(int givePoints) {
        this.givePoints = givePoints;
    }

    public void setAcPoints(int acPoints) {
        this.acPoints = acPoints;
    }

}

