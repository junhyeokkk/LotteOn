package com.team1.lotteon.entity;

import com.team1.lotteon.entity.enums.Gender;
import com.team1.lotteon.entity.enums.Grade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GeneralMember extends Member {
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Grade grade = Grade.E;
    private int points;
    private String email;
    private String ph;

    // 1: 정상 , 2: 중지, 3: 휴먼, 4: 탈퇴
    private int status=1;

    private LocalDate birth;
    @Embedded
    private Address address;
    private LocalDateTime lastLoginDate;
    private String etc;

    public void increasePoints(int givepoints) {
        this.points += givepoints;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
}
