package com.team1.lotteon.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name="member_id")
    private GeneralMember member;

    public void changeMember(GeneralMember member){
        this.member = member;
    }
}
