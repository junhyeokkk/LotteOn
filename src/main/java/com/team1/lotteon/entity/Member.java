package com.team1.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 유저의 기본 엔티티 생성
*/
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    private String uid;
    private String pass;
    private String role;
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public void setRole(String role) {
        this.role = role;
    }



}



