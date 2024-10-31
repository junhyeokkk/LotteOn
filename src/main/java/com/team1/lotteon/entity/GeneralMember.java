package com.team1.lotteon.entity;

import com.team1.lotteon.entity.enums.Gender;
import com.team1.lotteon.entity.enums.Grade;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/*
    날짜 : 2024/10/25
    이름 : 이상훈
    내용 : 멤버를 상속받는 일반회원 엔티티 생성

    수정이력
   - 2025/10/31 박서홍 - 상태변경코드 추가
*/
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class GeneralMember extends Member {
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Grade grade = Grade.FAMILY;
    private int points;
    private String email;
    private String ph;

    // 1: 정상 , 2: 중지, 3: 휴면, 4: 탈퇴
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
    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    // 상태를 중지로 변경
    public void suspend() {
        this.status = 2;
    }

    // 상태를 정상으로 변경
    public void reactivate() {
        this.status = 1;
    }

    // 현재 상태를 반환하는 메서드 (확인용)
    public boolean isSuspended() {
        return this.status == 2;
    }

    public boolean isActive() {
        return this.status == 1;
    }


}
